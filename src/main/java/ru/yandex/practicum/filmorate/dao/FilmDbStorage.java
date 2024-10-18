package ru.yandex.practicum.filmorate.dao;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAllFilms() {
        String findAllQuery = """
                SELECT * FROM film
            """;
        return jdbcTemplate.query(findAllQuery, FilmDbStorage::mapper);
    }

    @Override
    public Film createFilm(Film film) {
        String createQuery = """
            INSERT INTO film (name, description, release_date, duration, rating_id) VALUES (?, ?, ?, ?, ?)
            """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(createQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getRatingMPA() == null ? 0 : film.getRatingMPA().getId());
            return stmt;
        }, keyHolder);
        var key = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return Film.builder()
                .id(key)
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .ratingMPA(film.getRatingMPA())
                .build();
    }

    @Override
    public Film updateFilm(Film film) {
        String updateQuery = """
            UPDATE film
            SET name = ?, description = ?, release_date = ?, duration = ?
            WHERE id = ?
            """;
        jdbcTemplate.update(updateQuery, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getId());
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(Long id) {
        String findByIdQuery = """
                SELECT * FROM film WHERE id = ?
            """;
        return jdbcTemplate.queryForObject(findByIdQuery, FilmDbStorage::mapper, id);
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        String queryAddLike = """
                INSERT INTO film_like (USER_ID, FILM_ID) values (?,?)
            """;
        jdbcTemplate.update(queryAddLike, userId, filmId);
        return getFilmById(filmId);
    }

    @Override
    public Film removeLike(Long filmId, Long userId) {
        String queryRemoveLike = """
            DELETE FROM film_like WHERE film_id = ? and user_id = ?
        """;
        jdbcTemplate.update(queryRemoveLike, filmId, userId);
        return getFilmById(filmId);
    }

    @Override
    public List<Film> getTop10Films(int count) {
        String queryGetTop10Films = """
            SELECT f.*
            FROM film f join rating_mpa rm on rm.id = f.rating_id
            left join film_like fl on f.id = fl.film_id
            group by f.id, f.name
            order by COUNT(fl.user_id) DESC limit ?
        """;

        return jdbcTemplate.query(queryGetTop10Films, FilmDbStorage::mapper, count);
    }

    @Override
    public void addGenresToFilm(Long filmId, List<Genre> genres) {
        String queryAddGenresToFilm = """
                INSERT INTO film_genre (film_id, genre_id) values (?,?)
        """;

        jdbcTemplate.batchUpdate(
                queryAddGenresToFilm,
                new BatchPreparedStatementSetter() {
                    public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, filmId);
                        ps.setLong(2, genres.get(i).getId());
                    }
                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    private static Film mapper(ResultSet rs, int rowNum) throws SQLException {
       return Film.builder()
                .id(rs.getLong(1))
                .name(rs.getString(2))
                .description(rs.getString(3))
                .releaseDate(rs.getDate(4).toLocalDate())
                .duration(rs.getInt(5))
                .build();
    }
}
