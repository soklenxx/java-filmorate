package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.sql.ResultSet;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class RatingMpaDbStorage implements RatingMpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public RatingMpa findById(Long id) {
        String findRatingMPAByIdQuery = """
            SELECT * FROM rating_mpa WHERE id = ?
        """;
        try {
            return jdbcTemplate.queryForObject(findRatingMPAByIdQuery, (ResultSet rs, int rowNum) -> RatingMpa.builder()
                    .id(rs.getLong(1))
                    .name(rs.getString(2))
                    .build(), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Такого жанра нет в списке");
        }
    }

    public List<RatingMpa> findAll() {
        String findAllMpaQuery = """
            SELECT * FROM rating_mpa
        """;
        try {
            return jdbcTemplate.query(findAllMpaQuery, (ResultSet rs, int rowNum) -> RatingMpa.builder()
                    .id(rs.getLong(1))
                    .name(rs.getString(2))
                    .build());
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Список рейтингов пуст.");
        }
    }
}
