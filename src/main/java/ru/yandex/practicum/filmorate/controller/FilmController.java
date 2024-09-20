package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final int MAX_LENGTH_DESCRIPTION = 199;
    private final LocalDate MIN_DATE_RELEASE = LocalDate.parse("1995-12-28");
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Request to get Films");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Request to create Film by name {}", film.getName());
        // проверяем выполнение необходимых условий
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Empty Film name");
            throw new ConditionsNotMetException("Название не может быть пустым");
        }
        if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
            log.error("Maximum description length is 200 characters");
            throw new ConditionsNotMetException("Mаксимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isAfter(MIN_DATE_RELEASE)) {
            log.error("Release date: no earlier than December 28, 1895");
            throw new ConditionsNotMetException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.error("The length of the movie must be a positive number.");
            throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом.");
        }
        // формируем дополнительные данные
        film.setId(getNextId());
        // сохраняем новую публикацию в памяти приложения
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        // проверяем необходимые условия
        if (film.getId() == null) {
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        log.info("Request to update Film by id {}", film.getId());
        if (films.containsKey(film.getId())) {
            Film oldFilm = films.get(film.getId());
            if (film.getName() == null || film.getName().isEmpty()) {
                log.error("Empty Film name");
                throw new ConditionsNotMetException("Название не может быть пустым");
            }
            if (film.getDescription().length() > MAX_LENGTH_DESCRIPTION) {
                log.error("Maximum description length is 200 characters");
                throw new ConditionsNotMetException("Mаксимальная длина описания — 200 символов");
            }
            if (film.getReleaseDate().isAfter(MIN_DATE_RELEASE)) {
                log.error("Release date: no earlier than December 28, 1895");
                throw new ConditionsNotMetException("Дата релиза — не раньше 28 декабря 1895 года");
            }
            if (film.getDuration() < 0) {
                log.error("The length of the movie must be a positive number.");
                throw new ConditionsNotMetException("Продолжительность фильма должна быть положительным числом.");
            }
            oldFilm.setName(film.getName());
            oldFilm.setDuration(film.getDuration());
            oldFilm.setDescription(film.getDescription());
            oldFilm.setReleaseDate(film.getReleaseDate());
            return oldFilm;
            }

        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private int getNextId() {
        int currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
