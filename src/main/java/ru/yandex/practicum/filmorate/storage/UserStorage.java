package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    List<User> getAllUsers();

    void addUser(User user);

    void changeUSer(User user);

    boolean isValid(User user);

    User findUserById(int id);

    void addFriendToUser(int idUser, int idFriend);

    void deleteFromFriends(int idUser, int idFriend);

    List<User> getUserFriends(int id);

    User getUser(int id);
}
