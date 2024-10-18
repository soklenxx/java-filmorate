package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAllUsers() {
        String findAllQuery = """
                SELECT * FROM "USER"
            """;
        return jdbcTemplate.query(findAllQuery, UserDbStorage::mapper);
    }

    @Override
    public User createUser(User user) {
        String createQuery = """
            INSERT INTO "USER" (email, login, name, birthday) VALUES (?, ?, ?, ?)
            """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(createQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        var key = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return User.builder()
                .id(key)
                .email(user.getEmail())
                .login(user.getLogin())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null) {
            log.error("Empty User id");
            throw new ValidationException("Id должен быть указан");
        }
        getUserById(user.getId());
        String updateQuery = """
            UPDATE "USER"
            SET email = ?, login = ?, name = ?, birthday = ?
            WHERE id = ?
            """;
        jdbcTemplate.update(updateQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getUserById(user.getId());
    }

    @Override
    public User getUserById(Long id){
        String findByIdQuery = """
                SELECT * FROM "USER" WHERE id = ?
            """;
        User user = jdbcTemplate.query(findByIdQuery, UserDbStorage::mapper, id).stream().findAny().orElse(null);
        if(user == null) {
            log.debug("Пользователь с id={}  не найден.", id);
            throw new NotFoundException("Пользователь не найден.");
        }
        return user;
    }

    @Override
    public User addFriend(Long userId, Long friendId, boolean status) {
        String queryAddFriend = """
                INSERT INTO user_friendship (request_friend_id, response_friend_id, status) values (?,?,?)
            """;
        jdbcTemplate.update(queryAddFriend, userId, friendId, status);
        return getUserById(userId);
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        String queryRemoveFriend = """
                DELETE FROM user_friendship
                WHERE request_friend_id = ? and response_friend_id = ?
        """;
        jdbcTemplate.update(queryRemoveFriend, userId, friendId);
        return getUserById(userId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        String queryGetFriends = """
            SELECT * FROM "USER" where id IN
                (SELECT response_friend_id FROM user_friendship WHERE request_friend_id = ?)
                OR id IN (SELECT request_friend_id FROM user_friendship WHERE response_friend_id = ? AND status = true)
        """;
        getUserById(userId);

        return jdbcTemplate.query(queryGetFriends, UserDbStorage::mapper, userId, userId);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        String query = """
            SELECT * FROM "USER" WHERE (id IN (SELECT response_friend_id from user_friendship where request_friend_id = ?)
                OR id IN (SELECT request_friend_id FROM user_friendship WHERE response_friend_id = ? AND status = true))
                AND (id in (SELECT response_friend_id FROM user_friendship WHERE request_friend_id = ?)
                OR id IN (SELECT request_friend_id FROM user_friendship WHERE response_friend_id = ? AND status = true))
        """;

        return jdbcTemplate.query(query, UserDbStorage::mapper, friendId, userId, friendId, userId);
    }

    private static User mapper(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong(1))
                .email(rs.getString(2))
                .login(rs.getString(3))
                .name(rs.getString(4))
                .birthday(rs.getDate(5).toLocalDate())
                .build();
    }
}
