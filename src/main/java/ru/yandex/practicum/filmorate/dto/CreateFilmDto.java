package ru.yandex.practicum.filmorate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFilmDto {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Long ratingId;
}
