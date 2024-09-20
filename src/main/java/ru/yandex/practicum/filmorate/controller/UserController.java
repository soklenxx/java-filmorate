package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Request to get Users");
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Request to create User by login {}", user.getLogin());
        // проверяем выполнение необходимых условий
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Empty User email");
            throw new ConditionsNotMetException("Имейл должен быть указан");
        }
        if (!user.getEmail().contains("@")) {
            throw new ConditionsNotMetException("Имейл должен содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ConditionsNotMetException("Логин должен быть указан");
        }
        if (user.getLogin().contains(" ")) {
            throw new ConditionsNotMetException("Логин не должен содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
        }
        // формируем дополнительные данные
        user.setId(getNextId());
        // сохраняем новую публикацию в памяти приложения
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        // проверяем необходимые условия
        if (user.getId() == null) {
            log.error("Empty User id");
            throw new ConditionsNotMetException("Id должен быть указан");
        }
        log.info("Request to update User by id {}", user.getId());
        if (users.containsKey(user.getId())) {
            User oldUser = users.get(user.getId());
            if (oldUser.getEmail() == null || oldUser.getEmail().isBlank()) {
                log.error("Empty User email");
                throw new ConditionsNotMetException("Имейл должен быть указан");
            }
            if (!oldUser.getEmail().contains("@")) {
                throw new ConditionsNotMetException("Имейл должен содержать символ @");
            }
            if (oldUser.getLogin() == null || oldUser.getLogin().isBlank()) {
                throw new ConditionsNotMetException("Логин должен быть указан");
            }
            if (oldUser.getLogin().contains(" ")) {
                throw new ConditionsNotMetException("Логин не должен содержать пробелы");
            }
            if (oldUser.getName() == null || oldUser.getName().isBlank()) {
                oldUser.setName(user.getLogin());
            }
            if (oldUser.getBirthday().isAfter(LocalDate.now())) {
                throw new ConditionsNotMetException("Дата рождения не может быть в будущем");
            }
            oldUser.setName(user.getName());
            oldUser.setBirthday(user.getBirthday());
            oldUser.setLogin(user.getLogin());
            oldUser.setEmail(user.getEmail());
            return oldUser;
        }
        throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
