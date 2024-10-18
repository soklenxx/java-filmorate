package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.util.List;

public interface RatingMpaStorage {
    RatingMpa findById(Long id);

    List<RatingMpa> findAll();
}
