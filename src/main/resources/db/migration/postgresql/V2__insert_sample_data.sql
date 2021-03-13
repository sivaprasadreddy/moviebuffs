INSERT INTO roles (id, name, created_at) VALUES
(1, 'ROLE_ADMIN', CURRENT_TIMESTAMP),
(2, 'ROLE_USER', CURRENT_TIMESTAMP)
;

INSERT INTO users (email, password, name, created_at) VALUES
('admin@gmail.com', '$2a$10$ZuGgeoawgOg.6AM3QEGZ3O4QlBSWyRx3A70oIcBjYPpUB8mAZWY16', 'Admin', CURRENT_TIMESTAMP),
('siva@gmail.com', '$2a$10$CIXGKN9rPfV/mmBMYas.SemoT9mfVUUwUxueFpU3DcWhuNo5fexYC', 'Siva',  CURRENT_TIMESTAMP),
('prasad@gmail.com', '$2a$10$vtnCx8LxraSbveB26Lth3.s/.9hI1SFHwCFTSlAkAlVRybva6GQo6', 'Prasad',  CURRENT_TIMESTAMP)
;

INSERT INTO user_role (user_id, role_id) VALUES
(1, 1),
(1, 2),
(2, 2),
(3, 2)
;

INSERT INTO GENRES(ID, TMDB_ID, NAME, SLUG) VALUES
(1,	28, 'Action', 'action'),
(2,	12, 'Adventure', 'adventure'),
(3,	16, 'Animation', 'animation'),
(4,	35, 'Comedy', 'comedy'),
(5,	80, 'Crime', 'crime'),
(6, 99, 'Documentary',	'documentary'),
(7,	18, 'Drama', 'drama'),
(8,	10751, 'Family', 'family'),
(9,	14, 'Fantasy', 'fantasy'),
(10, 36, 'History', 'history'),
(11, 27, 'Horror', 'horror'),
(12, 10402, 'Music', 'music'),
(13, 9648, 'Mystery', 'mystery'),
(14, 10749, 'Romance', 'romance'),
(15, 878, 'Science Fiction', 'science-fiction'),
(16, 10770, 'TV Movie', 'tv-movie'),
(17, 53, 'Thriller', 'thriller'),
(18, 10752, 'War', 'war'),
(19, 37,'Western', 'western'),
(20, null, 'Foreign', 'foreign')
;