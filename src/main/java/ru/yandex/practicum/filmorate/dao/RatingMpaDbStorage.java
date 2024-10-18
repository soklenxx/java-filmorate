package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.RatingMpa;
import ru.yandex.practicum.filmorate.storage.RatingMpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
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
            return jdbcTemplate.queryForObject(findRatingMPAByIdQuery, RatingMpaDbStorage::mapperRatingMpa, id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Такого рейтинга нет в списке");
        }
    }

    public List<RatingMpa> findAll() {
        String findAllMpaQuery = """
            SELECT * FROM rating_mpa
        """;
        try {
            return jdbcTemplate.query(findAllMpaQuery, RatingMpaDbStorage::mapperRatingMpa);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Список рейтингов пуст.");
        }
    }

    private static RatingMpa mapperRatingMpa(ResultSet rs, int rowNum) throws SQLException {
        return RatingMpa.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .build();
    }
}
