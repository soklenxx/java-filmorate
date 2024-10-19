package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.service.RatingMpaService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class RatingMPAController {
    private final RatingMpaService ratingMpaService;

    @GetMapping
    public ResponseEntity<List<RatingMpa>> findAll() {
        return ResponseEntity.ok(ratingMpaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingMpa> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ratingMpaService.findById(id));
    }
}
