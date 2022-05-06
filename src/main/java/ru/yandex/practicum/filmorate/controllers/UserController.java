package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@RequestBody User user) throws ValidationException {
        userService.addUser(user);
        log.info("Добавлен пользователь " + user.getName());
        return user;
    }

    @PutMapping
    public void changeUser(@RequestBody User user) {
        userService.changeUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriendToUser(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteUserFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFromFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriend(id, otherId);
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }
}
