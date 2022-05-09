package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.CreatorId;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//не совсем понимаю практический смысл этого класса
//мне кажется все что тут есть можно было бы сделать в UserService
@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> allUsers = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<User>(allUsers.values());
    }

    @Override
    public void addUser(User user) {
        if (isValid(user)) {
            user.setId(CreatorId.createUserId());
            allUsers.put(user.getId(), user);
        }
    }

    @Override
    public void changeUSer(User user) {
        if (isValid(user)) {
            if (allUsers.containsKey(user.getId())) {
                allUsers.put(user.getId(), user);
            } else {
                addUser(user);
            }
        }
    }

    @Override
    public boolean isValid(User user) {
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
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            return true;
        }
    }

    @Override
    public User findUserById(int id) {
        return allUsers.get(id);
    }

    @Override
    public void addFriendToUser(long idUser, long friendId) {
        if(allUsers.containsKey(idUser) && allUsers.containsKey(friendId)) {
            allUsers.get(idUser).addFriend(friendId);
        } else {
            throw new UserNotFoundException("One of that id isn't correct " + idUser + " " + friendId);
        }
    }

    @Override
    public void deleteFromFriends(long idUser, long friendId) {
        allUsers.get(idUser).deleteFriend(friendId);
    }

    @Override
    public List<User> getUserFriends(long id) {
        Set<Long> set = allUsers.get(id).getFriends();
        return allUsers.values().stream()
                .filter(x -> set.contains(x.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public User getUser(long id) {
        if(allUsers.containsKey(id)) {
            return allUsers.get(id);
        } else {
            throw new UserNotFoundException("User not found " + id);
        }
    }
}
