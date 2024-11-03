DELETE FROM film_like;
DELETE FROM film_genre;
DELETE FROM user_friendship;
DELETE FROM "USER";
ALTER TABLE "USER" ALTER COLUMN ID RESTART WITH 1;
DELETE FROM film;
ALTER TABLE film ALTER COLUMN ID RESTART WITH 1;
DELETE FROM genre;
ALTER TABLE genre ALTER COLUMN ID RESTART WITH 1;
DELETE FROM rating_mpa;
ALTER TABLE rating_mpa ALTER COLUMN ID RESTART WITH 1;
INSERT INTO genre (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');
INSERT INTO rating_mpa (name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');