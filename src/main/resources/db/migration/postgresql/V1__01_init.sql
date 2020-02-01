create sequence movie_id_seq start with 1 increment by 1;
create sequence cast_id_seq start with 1 increment by 1;
create sequence crew_id_seq start with 1 increment by 1;
create sequence genre_id_seq start with 1 increment by 1;

create table movies (
    id bigint DEFAULT nextval('movie_id_seq') not null,
    title varchar(255),
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
    movie_id varchar(255),
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
    movie_id varchar(255),
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

INSERT INTO GENRES(ID, NAME, SLUG) VALUES
(1,	'Animation', 'animation'),
(2,	'Comedy', 'comedy'),
(3,	'Family', 'family'),
(4,	'Adventure', 'adventure'),
(5,	'Fantasy', 'fantasy'),
(6,	'Romance', 'romance'),
(7,	'Drama', 'drama'),
(8,	'Action', 'action'),
(9,	'Crime', 'crime'),
(10, 'Thriller', 'thriller'),
(11, 'Horror', 'horror'),
(12, 'History', 'history'),
(13, 'Science Fiction', 'science-fiction'),
(14, 'Mystery', 'mystery'),
(15, 'War', 'war'),
(16, 'Foreign', 'foreign'),
(17, 'Music', 'music'),
(18, 'Documentary',	'documentary'),
(19, 'Western', 'western'),
(20, 'TV Movie', 'tv-movie')
;