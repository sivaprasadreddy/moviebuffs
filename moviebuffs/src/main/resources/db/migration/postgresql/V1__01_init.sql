create sequence user_id_seq start with 1 increment by 1;
create sequence role_id_seq start with 1 increment by 1;
create sequence movie_id_seq start with 1 increment by 1;
create sequence cast_id_seq start with 1 increment by 1;
create sequence crew_id_seq start with 1 increment by 1;
create sequence genre_id_seq start with 1 increment by 1;

create sequence order_id_seq start with 1 increment by 1;
create sequence order_item_id_seq start with 1 increment by 1;

create table users (
    id bigint DEFAULT nextval('user_id_seq') not null,
    email varchar(255) not null,
    password varchar(255) not null,
    name varchar(255) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id),
    CONSTRAINT user_email_unique UNIQUE(email)
);

create table roles (
    id bigint DEFAULT nextval('role_id_seq') not null,
    name varchar(255) not null,
    created_at timestamp,
    updated_at timestamp,
    primary key (id),
    CONSTRAINT role_name_unique UNIQUE(name)
);

create table user_role (
    user_id bigint REFERENCES users(id),
    role_id bigint REFERENCES roles(id)
);

create table movies (
    id bigint DEFAULT nextval('movie_id_seq') not null,
    title varchar(255),
    tmdb_id varchar(255),
    imdb_id varchar(255),
    release_date date,
    poster_path varchar(512),
    original_language varchar(255),
    homepage varchar(512),
    budget varchar(255),
    revenue varchar(255),
    runtime varchar(255),
    tagline varchar(512),
    overview varchar(1024),
    vote_average numeric,
    vote_count numeric,
    price numeric not null default 0,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);

create table movie_cast (
    id bigint DEFAULT nextval('cast_id_seq') not null,
    cast_id varchar(255),
    character varchar(512),
    credit_id varchar(255),
    gender varchar(512),
    uid varchar(255),
    name varchar(512),
    cast_order varchar(255),
    profile_path varchar(255),
    movie_id bigint,
    primary key (id)
);

create table movie_crew (
    id bigint DEFAULT nextval('crew_id_seq') not null,
    credit_id varchar(255),
    department varchar(512),
    gender varchar(512),
    uid varchar(255),
    job varchar(512),
    name varchar(512),
    profile_path varchar(255),
    movie_id bigint,
    primary key (id)
);

create table genres (
    id bigint DEFAULT nextval('genre_id_seq') not null,
    name varchar(255) not null,
    slug varchar(255) not null,
    primary key (id),
    CONSTRAINT genre_name_unique UNIQUE(name),
    CONSTRAINT genre_slug_unique UNIQUE(slug)
);

create table movie_genre (
    movie_id bigint REFERENCES movies(id),
    genre_id bigint REFERENCES genres(id)
);

create table orders (
    id bigint DEFAULT nextval('order_id_seq') not null,
    order_id varchar(100),
    customer_email varchar(100),
    customer_name varchar(100),
    delivery_address varchar(255),
    credit_card_number varchar(50),
    cvv varchar(10),
    status varchar(100),
    created_by bigint REFERENCES users(id),
    created_at timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp WITH TIME ZONE,
    primary key (id)
);

create table order_items (
    id bigint DEFAULT nextval('order_item_id_seq') not null,
    product_code varchar(255) not null,
    product_name varchar(1024) not null,
    product_price numeric not null,
    quantity integer not null,
    order_id bigint not null references orders(id),
    created_at timestamp WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp WITH TIME ZONE,
    primary key (id)
);
