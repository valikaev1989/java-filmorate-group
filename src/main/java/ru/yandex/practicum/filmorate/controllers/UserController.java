package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Validated
public class UserController {
    private HashMap<Integer, User> allUsers = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        for (User user : allUsers.values()) {
            result.add(user);
        }
        return result;
    }

    @PostMapping
    public void addUser(@RequestBody User user) throws ValidationException {
        if(isValid(user)) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            allUsers.put(user.getId(), user);
            log.info("Добавлен пользователь " + user.getName());
        }
    }

    @PutMapping
    public void changeUser(@RequestBody User user) {
        if(isValid(user)) {
            addUser(user);
        }
    }

    private boolean isValid(User user) {
        if (user.getEmail().isEmpty() | !user.getEmail().contains("@")) {
            log.warn("Неккоректный email пользователя");
            throw new ValidationException("Поле адреса пустое или некорректный адрес");
        } else if (StringUtils.containsWhitespace(user.getLogin()) | user.getLogin().isEmpty()) {
            log.warn("Логин пустой или с пробелами");
            throw new ValidationException("Логин не может быть пустым или иметь пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения пользователя в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        } else {
            return true;
        }
    }
}
