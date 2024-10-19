package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre findById(Long id) {
        String sql = """
            SELECT * FROM genre WHERE id = ?
        """;
        try {
            return jdbcTemplate.queryForObject(sql, GenreDbStorage::toMapGenre, id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Такого жанра нет в списке");
        }
    }

    @Override
    public List<Genre> findAll() {
        String sql = """
            SELECT * FROM genre
        """;
        try {
            return jdbcTemplate.query(sql,GenreDbStorage::toMapGenre);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Список жанров пуст.");
        }
    }

    private static Genre toMapGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .build();
    }
}
