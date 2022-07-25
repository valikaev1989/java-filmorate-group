package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    List<User> getAllUsers();

    User addUser(User user);

    User changeUSer(User user);

    User findUserById(long id);

    List<User> getUserFriends(long id);

    boolean deleteUser(long id);
}