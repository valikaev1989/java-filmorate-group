package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendStorage {

    void addFriendToUser(long userId, long friendId);

    void deleteFromFriends(long userId, long friendId);

    List<Long> getUserFriends(long id);
}