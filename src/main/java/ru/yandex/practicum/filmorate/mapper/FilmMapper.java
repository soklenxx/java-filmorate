package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.CreateFilmDto;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmDto;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
public class FilmMapper {
    public Film toMap(CreateFilmDto dto) {
        return Film.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .genres(dto.getGenres())
                .mpa(dto.getMpa())
                .build();

    }

    public Film toMap(UpdateFilmDto dto) {
        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .build();

    }

    public FilmDto toMap(Film dto) {
        return FilmDto.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .genres(dto.getGenres())
                .mpa(dto.getMpa())
                .build();

    }

    public List<FilmDto> toMap(List<Film> films) {
        return films.stream()
                .map(this::toMap)
                .toList();
    }
}
