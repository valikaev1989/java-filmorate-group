package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {

    List<User> getAllUsers();

    User addUser(User user);

    User changeUSer(User user);

    boolean isValid(User user);

    User findUserById(int id);

    void addFriendToUser(long idUser, long idFriend);

    void deleteFromFriends(long idUser, long idFriend);

    List<User> getUserFriends(long id);

    User getUser(long id);
}
