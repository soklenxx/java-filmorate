package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingMpaService {
    private final RatingMpaStorage ratingMpaStorage;

    public RatingMpa findById(Long id) {
        return ratingMpaStorage.findById(id);
    }

    public List<RatingMpa> findAll() {
        return ratingMpaStorage.findAll();
    }
}
