CREATE TABLE IF NOT EXISTS film (
    id int generated by default as identity NOT NULL,
    name varchar(100) NOT NULL,
    description varchar(100) NOT NULL,
    release_date date NOT NULL,
    duration int NOT NULL,
    rating_id int,
    PRIMARY KEY (ID)
);
CREATE TABLE IF NOT EXISTS "USER" (
    id int generated by default as identity NOT NULL,
    email varchar(100) NOT NULL,
    login varchar(100) NOT NULL,
    name varchar(100) NOT NULL,
    birthday date NOT NULL,
    PRIMARY KEY (id)
    );
CREATE TABLE IF NOT EXISTS genre (
    id int generated by default as identity NOT NULL,
    name varchar(100) NOT NULL,
    PRIMARY KEY (id)
    );
CREATE TABLE IF NOT EXISTS film_genre (
    id int generated by default as identity NOT NULL,
    film_id int NOT NULL,
    genre_id int NOT NULL,
    PRIMARY KEY (id)
    );
CREATE TABLE IF NOT EXISTS film_like (
    id int generated by default as identity NOT NULL,
    film_id int NOT NULL,
    user_id int NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS rating_mpa (
    id int generated by default as identity NOT NULL,
    name varchar(100) NOT NULL,
    PRIMARY KEY (id)
);
CREATE TABLE IF NOT EXISTS user_friendship (
    id int generated by default as identity NOT NULL,
    request_friend_id int NOT NULL,
    response_friend_id int NOT NULL,
    status bool NOT NULL,
    PRIMARY KEY (id)
);
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