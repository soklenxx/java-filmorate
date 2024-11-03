package ru.yandex.practicum.filmorate.mapper;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dto.CreateUserDto;
import ru.yandex.practicum.filmorate.dto.UpdateUserDto;
import ru.yandex.practicum.filmorate.dto.UserDto;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public class UserMapper {
    public User toMap(CreateUserDto dto) {
        return User.builder()
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName())
                .birthday(dto.getBirthday())
                .build();

    }

    public User toMap(UpdateUserDto dto) {
        return User.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName())
                .birthday(dto.getBirthday())
                .build();

    }

    public UserDto toMap(User dto) {
        return UserDto.builder()
                .id(dto.getId())
                .email(dto.getEmail())
                .login(dto.getLogin())
                .name(dto.getName())
                .birthday(dto.getBirthday())
                .build();

    }

    public List<UserDto> toMap(List<User> films) {
        return films.stream()
                .map(this::toMap)
                .toList();
    }
}
