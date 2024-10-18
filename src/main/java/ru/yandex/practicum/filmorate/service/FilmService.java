package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final RatingMpaStorage ratingMpaStorage;
    private final GenreStorage genreStorage;

    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage, RatingMpaStorage ratingMpaStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.ratingMpaStorage = ratingMpaStorage;
        this.genreStorage = genreStorage;
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {
        if (film.getMpa() != null) {
            try {
                ratingMpaStorage.findById(film.getMpa().getId());
            } catch (NotFoundException e) {
                throw new ValidationException(e.getMessage());
            }
        }
        Film filmDb = filmStorage.createFilm(film);
        List<Genre> genres = film.getGenres();
        if (genres != null) {
            for(Genre genre : genres) {
                try {
                    genreStorage.findById(genre.getId());
                } catch (NotFoundException e) {
                    throw new ValidationException(e.getMessage());
                }
            }
            filmStorage.addGenresToFilm(filmDb.getId(), genres);
            filmDb.setGenres(genres);
        }
        return filmDb;
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film addLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        filmStorage.addLike(film.getId(), user.getId());
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        filmStorage.removeLike(film.getId(), user.getId());
        return film;
    }

    public List<Film> getTop10Films(int count) {
        return filmStorage.getTop10Films(count);
    }

}
