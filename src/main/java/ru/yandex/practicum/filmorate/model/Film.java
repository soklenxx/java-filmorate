package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Set<Long> likesUsers;

    public int getLikesUsersSize() {
        return likesUsers.size();
    }
}
