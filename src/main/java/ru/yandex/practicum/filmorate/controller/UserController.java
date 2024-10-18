package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.UpdateFilmDto;
import ru.yandex.practicum.filmorate.dto.UpdateUserDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(userMapper.map(userService.findAllUsers()));
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody CreateUserDto createUserDto) {
        User user = userMapper.map(createUserDto);
        validation(user);
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(userMapper.map(createdUser));
    }

    @PutMapping
    public ResponseEntity<UserDto> update(@RequestBody UpdateUserDto updateUserDto) {
        User user = userMapper.map(updateUserDto);
        validation(user);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(userMapper.map(updatedUser));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDto> addFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        return ResponseEntity.ok(userMapper.map(userService.addFriend(id, friendId)));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDto> removeFriend(@PathVariable("id") Long id, @PathVariable("friendId") Long friendId) {
        return ResponseEntity.ok(userMapper.map(userService.removeFriend(id, friendId)));
    }

    @GetMapping("{id}/friends")
    public ResponseEntity<List<UserDto>> getFriends(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userMapper.map(userService.getFriends(id)));
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<List<UserDto>> getCommonFriends(@PathVariable("id") Long id, @PathVariable("otherId") Long otherId) {
        return ResponseEntity.ok(userMapper.map(userService.getCommonFriends(id, otherId)));
    }

    private void validation(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.error("Empty User email");
            throw new ValidationException("Имейл должен быть указан");
        }
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Имейл должен содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("Логин должен быть указан");
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
