package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAllUsers();

    User createUser(User user);

    User updateUser(User user);

    User getUserById(Long id);

    void addFriend(Long userId, Long friendId, boolean status);

    void removeFriend(Long userId, Long friendId);

    List<User> getFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long friendId);
}
