package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.dto.CreateUserDto;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void succeedCreateFilm() {
        CreateUserDto user = CreateUserDto.builder()
                .email("soklenx@yandex.ru")
                .login("soklenx")
                .name("soklenx")
                .birthday(LocalDate.of(1999, 7, 3))

                .build();
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.login").value(user.getLogin()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(user.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.birthday").value(user.getBirthday().toString()));
    }
}