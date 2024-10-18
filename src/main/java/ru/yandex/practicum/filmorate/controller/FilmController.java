package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dto.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final FilmMapper filmMapper;

    private final int MAX_LENGTH_DESCRIPTION = 200;
    private final LocalDate MIN_DATE_RELEASE = LocalDate.parse("1895-12-28");

    @GetMapping
    public ResponseEntity<List<FilmDto>> findAll() {
        return ResponseEntity.ok(filmMapper.map(filmService.findAllFilms()));
    }

    @PostMapping
    public ResponseEntity<FilmDto> create(@RequestBody CreateFilmDto createFilmDto) {
        Film film = filmMapper.map(createFilmDto);
        validation(film);
        Film createdFilm = filmService.createFilm(film);
        return ResponseEntity.ok(filmMapper.map(createdFilm));
    }

    @PutMapping
    public ResponseEntity<FilmDto> update(@RequestBody UpdateFilmDto updateFilmDto) {
        Film film = filmMapper.map(updateFilmDto);
        validation(film);
        Film updatedFilm = filmService.updateFilm(film);
        return ResponseEntity.ok(filmMapper.map(updatedFilm));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<FilmDto> addLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        Film addLike = filmService.addLike(id, userId);
        return ResponseEntity.ok(filmMapper.map(addLike));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<FilmDto> removeLike(@PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        Film removeLike = filmService.removeLike(id, userId);
        return ResponseEntity.ok(filmMapper.map(removeLike));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<FilmDto>> getTop10Films(@RequestParam(required = false, defaultValue = "10") int count) {
        return ResponseEntity.ok(filmMapper.map(filmService.getTop10Films(count)));
    }

    private void validation(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isEmpty()) {
            log.error("Empty Film name");
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() >= MAX_LENGTH_DESCRIPTION) {
            log.error("Maximum description length is 200 characters");
            throw new ValidationException("Mаксимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(MIN_DATE_RELEASE)) {
            log.error("Release date: no earlier than December 28, 1895");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.error("The length of the movie must be a positive number.");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }

}