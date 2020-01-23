create sequence movie_id_seq start with 1 increment by 1;

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
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
);
