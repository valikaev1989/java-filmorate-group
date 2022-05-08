package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.CreatorId;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * чтобы тесты не падали мне пришлось написать 2 метода в классе CreatorId, которые устанавливают
 * айдишники на 1 (типо обнуляют счетчик). Не очень мне это нравится, но лучше ничего в голову не пришло
 */
class FilmControllerTest {
    private FilmController controller = new FilmController(new FilmService(new InMemoryFilmStorage()));

    @BeforeEach
    void createController() {
        controller = new FilmController(new FilmService(new InMemoryFilmStorage()));
        Film filmOne = new Film(1,
            "One",
            "description",
            LocalDate.of(2022, 01, 01),
            Duration.ofMinutes(160));

        Film filmTwo = new Film(2,
                "Two",
                "description",
                LocalDate.of(2022, 01, 01),
                Duration.ofMinutes(160));
        controller.addFilm(filmOne);
        controller.addFilm(filmTwo);
    }

    @AfterEach
    void clear() {
        controller = null;
        CreatorId.setFilmId();
    }

    @Test
    void shouldNotValidateFilmEmptyName() {
        Film film = new Film(
                1,
                "",
                "description",
                LocalDate.of(2022, 01, 01),
                Duration.ofMinutes(160));

        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }

    @Test
    void shouldNotValidateFilmBlankName() {
        Film film = new Film(
                1,
                "    ",
                "description",
                LocalDate.of(2022, 01, 01),
                Duration.ofMinutes(160));

        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }

    @Test
    void shouldNotValidateFilmBigDescription() {
        Film film = new Film(
                1,
                "film",
                "оооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооо" +
                        "ооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооооо" +
                        "оооооооооооооооооо",
                LocalDate.of(2022, 01, 01),
                Duration.ofMinutes(160));

        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }

    @Test
    void shouldNotValidateFilmEarlyDate() {
        Film film = new Film(
                1,
                "film",
                "description",
                LocalDate.of(1895, 12, 01),
                Duration.ofMinutes(160));

        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }

    @Test
    void shouldNotValidateFilmNegativeDuration() {
        Film film = new Film(
                1,
                "film",
                "description",
                LocalDate.of(2000, 12, 01),
                Duration.ofMinutes(-10));

        assertThrows(ValidationException.class, () -> controller.addFilm(film));
    }

    @Test
    void shouldBeOneLikes() {
        controller.like(1, 1);

        assertEquals(1, controller.getFilms().get(0).getLikes().size());
    }

    @Test
    void shouldBeZeroDeleteLike() {
        controller.like(1, 1);
        controller.deleteLike(1, 1);

        assertEquals(0, controller.getFilms().get(0).getLikes().size());
    }

    @Test
    void shouldFilmTwoBeInFirstPlace() {
        controller.like(2, 1);
        assertEquals("Two", controller.getPopularFilms(10).get(0).getName());
    }
}
