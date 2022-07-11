package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public UserService(@Qualifier("userDBStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User changeUser(User user) {
        return userStorage.changeUSer(user);
    }

    public void addFriendToUser(long idUser, long friendId) {
        userStorage.addFriendToUser(idUser, friendId);
        //userStorage.addFriendToUser(friendId, idUser);
    }

    public void deleteFromFriend(long idUser, long friendId) {
        userStorage.deleteFromFriends(idUser, friendId);
        userStorage.deleteFromFriends(friendId, idUser);
    }

    public List<User> getUserFriends(long id) {
        return userStorage.getUserFriends(id);
    }

    public List<User> getCommonFriend(long id, long otherId) {
        List<User> user = getUserFriends(id);
        List<User> anotherUser = getUserFriends(otherId);
        return user.stream().filter(x -> anotherUser.contains(x)).collect(Collectors.toList());
    }

    public User getUser(long id) {
        return userStorage.findUserById(id);
    }
}
