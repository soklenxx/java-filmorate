package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmStorage {
    List<Film> findAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(Long id);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Film> getTop10Films(int count);

    void addGenresToFilm(Long filmId, List<Genre> genres);


}
