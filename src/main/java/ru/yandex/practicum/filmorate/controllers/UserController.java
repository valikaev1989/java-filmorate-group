package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventsService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private final UserService userService;
    private final EventsService eventsService;

    @Autowired
    public UserController(UserService userService, EventsService eventsService) {
        this.userService = userService;
        this.eventsService = eventsService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Returned all users");
        return userService.getAllUsers();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        User result = userService.addUser(userService.checkName(user));
        log.info("User was added " + result.getName());
        return result;
    }

    @PutMapping
    public User changeUser(@Valid @RequestBody User user) {
        log.info("User was changed " + user.getName() + " " + user.getId());
        return userService.changeUser(userService.checkName(user));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info(String.format("User %d was added like friend to user %d", friendId, id));
        userService.addFriendToUser(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteUserFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("User %d was deleted from %d", friendId, id);
        userService.deleteFromFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable long id) {
        log.info(String.format("Get all the user's %d friends", id));
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info(String.format("Get common friends between user %d and user %d", id, otherId));
        return userService.getCommonFriend(id, otherId);
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable long id) {
        log.info(String.format("Get user %d", id));
        return userService.getUserById(id);
    }
    @GetMapping("/{id}/feed")
    public List<Event> getEvents(@PathVariable("id") Long id) {
        return eventsService.getEvents(id);
    }
}
