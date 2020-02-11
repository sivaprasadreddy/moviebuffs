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
