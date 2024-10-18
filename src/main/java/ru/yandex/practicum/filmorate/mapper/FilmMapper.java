package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
public class FilmMapper {
    public Film map(CreateFilmDto dto) {
        return Film.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .build();

    }

    public Film map(UpdateFilmDto dto) {
        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .build();

    }

    public FilmDto map(Film dto) {
        return FilmDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .genres(dto.getGenres())
                .ratingMPA(dto.getRatingMPA())
                .build();

    }

    public List<FilmDto> map(List<Film> films) {
        return films.stream()
                .map(this::map)
                .toList();
    }
}
