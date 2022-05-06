package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void addUser(User user) {
        userStorage.addUser(user);
    }

    public void changeUser(User user) {
        userStorage.changeUSer(user);
    }

    public void addFriendToUser(int idUser, int friendId) {
        userStorage.addFriendToUser(idUser, friendId);
        userStorage.addFriendToUser(friendId, idUser);
    }

    public void deleteFromFriend(int idUser, int friendId) {
        userStorage.deleteFromFriends(idUser, friendId);
        userStorage.deleteFromFriends(friendId, idUser);
    }

    public List<User> getUserFriends(int id) {
        return userStorage.getUserFriends(id);
    }

    public List<User> getCommonFriend(int id, int otherId) {
        List<User> user = getUserFriends(id);
        List<User> anotherUser = getUserFriends(otherId);
        return user.stream().filter(x -> anotherUser.contains(x)).collect(Collectors.toList());
    }

    public User getUser(int id) {
        return userStorage.getUser(id);
    }
}
