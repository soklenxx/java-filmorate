package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> findAllFilms();
    Film createFilm(Film film);
    Film updateFilm(Film film);
    Film getFilmById(Long id);
}
