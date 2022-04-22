package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private final UserController controller = new UserController();

    @Test
    void shouldNotValidateEmptyEmail() {
        User user = new User(1,
                "",
                "login",
                "name",
                LocalDate.of(1994, 8, 21));

        assertThrows(ValidationException.class, ()-> controller.addUser(user));
    }

    @Test
    void shouldNotValidateEmail() {
        User user = new User(1,
                "boo.ru",
                "login",
                "name",
                LocalDate.of(1994, 8, 21));

        assertThrows(ValidationException.class, ()-> controller.addUser(user));
    }

    @Test
    void shouldNotValidateEmptyLogin() {
        User user = new User(1,
                "boo@ya.ru",
                "",
                "name",
                LocalDate.of(1994, 8, 21));

        assertThrows(ValidationException.class, ()-> controller.addUser(user));
    }

    @Test
    void shouldNotValidateLoginWithWhitespace() {
        User user = new User(1,
                "boo@ya.ru",
                "login with gap",
                "name",
                LocalDate.of(1994, 8, 21));

        assertThrows(ValidationException.class, ()-> controller.addUser(user));
    }

    @Test
    void shouldNotValidateBithdateInFuture() {
        User user = new User(1,
                "boo@ya.ru",
                "login",
                "name",
                LocalDate.of(3000, 8, 21));

        assertThrows(ValidationException.class, ()-> controller.addUser(user));
    }
}