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
import ru.yandex.practicum.filmorate.dto.CreateFilmDto;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.RatingMpa;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void succeedCreateFilm() {
        CreateFilmDto film = CreateFilmDto.builder()
                .name("Матрица")
                .description("Научно-фантастический боевик")
                .releaseDate(LocalDate.of(1999, 7, 3))
                .duration(136)
                .mpa(RatingMpa.builder().id(1L).build())
                .genres(List.of(Genre.builder().id(1L).build()))
                .build();
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(film));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(film.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(film.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.releaseDate").value(film.getReleaseDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.duration").value(film.getDuration()));
    }
}