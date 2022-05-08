package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.CreatorId;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private UserController controller;

    @BeforeEach
    void createController() {
        controller = new UserController(new UserService(new InMemoryUserStorage()));
        User userOne = new User(1, "mail@mail.com", "login", "One",
                LocalDate.of(1994, 8, 21));

        User userTwo = new User(2, "mail@mail.com", "login", "Two",
                LocalDate.of(1994, 8, 21));
        User userThree = new User(3, "mail@mail.com", "login", "Three",
                LocalDate.of(1994, 8, 21));
        controller.addUser(userOne);
        controller.addUser(userTwo);
        controller.addUser(userThree);
    }

    @AfterEach
    void clear() {
        controller = null;
        CreatorId.setUserId();
    }

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
    void shouldNotValidateBirthdateInFuture() {
        User user = new User(1,
                "boo@ya.ru",
                "login",
                "name",
                LocalDate.of(3000, 8, 21));

        assertThrows(ValidationException.class, ()-> controller.addUser(user));
    }

    @Test
    void shouldBeOneFriend() {
        controller.addToFriend(1, 2);

        assertEquals(1, controller.getUserFriends(1).size());
    }

    @Test
    void shouldBeZeroFriends() {
        controller.addToFriend(1, 2);
        controller.deleteUserFriend(1, 2);

        assertEquals(0, controller.getUserFriends(1).size());
    }

    @Test
    void shouldBeUserOneCommonFriend() {
        controller.addToFriend(1, 2);
        controller.addToFriend(1, 3);

        assertEquals("One", controller.getCommonFriends(2, 3).get(0).getName());
    }
}