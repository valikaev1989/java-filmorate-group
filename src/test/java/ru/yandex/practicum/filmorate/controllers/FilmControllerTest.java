package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private final FilmController controller = new FilmController();

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
}