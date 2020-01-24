create sequence movie_id_seq start with 1 increment by 1;
create sequence cast_id_seq start with 1 increment by 1;
create sequence crew_id_seq start with 1 increment by 1;
create sequence genre_id_seq start with 1 increment by 1;

create table movies (
    id bigint default movie_id_seq.nextval,
    title varchar(255),
    imdb_id varchar(255),
    release_date varchar(255),
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
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);

create table cast (
    id bigint default cast_id_seq.nextval,
    cast_id varchar(255),
    character varchar(512),
    credit_id varchar(255),
    gender varchar(512),
    uid varchar(255),
    name varchar(512),
    cast_order varchar(255),
    profile_path varchar(255),
    movie_id varchar(255),
    primary key (id)
);

create table crew (
    id bigint default crew_id_seq.nextval,
    credit_id varchar(255),
    department varchar(512),
    gender varchar(512),
    uid varchar(255),
    job varchar(512),
    name varchar(512),
    profile_path varchar(255),
    movie_id varchar(255),
    primary key (id)
);


create table genres (
    id bigint default genre_id_seq.nextval,
    name varchar(255) not null,
    slug varchar(255) not null,
    primary key (id),
    UNIQUE KEY genre_name_unique (name),
    UNIQUE KEY genre_slug_unique (slug)
);

create table movie_genre (
    movie_id bigint REFERENCES movies(id),
    genre_id bigint REFERENCES genres(id)
);
