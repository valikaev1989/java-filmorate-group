/*
package ru.yandex.practicum.filmorate.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    @Order(1)
    public void testFindUserById() {

        User user = new User("g@mail.ru", "new login", "new name", LocalDate.of(1999, 8, 23));
        userStorage.addUser(user);
        user.setId(1);

        assertEquals(user, userStorage.findUserById(1));
        userStorage.deleteAllUsers();
    }

    @Test
    @Order(2)
    //обавить удаление из базы чтобы обнулить все
    public void testAddUser() {
        User user = new User("g@mail.ru", "new login", "new name", LocalDate.of(1999, 8, 23));
        User indexUser = userStorage.addUser(user);
        //user.setId(1);
        System.out.println(indexUser.getId());
        assertEquals(1, userStorage.getAllUsers().size());
        userStorage.deleteUser(1);
    }

    @Test
    @Order(3)
    public void testGetAllUsers() {
        userStorage.deleteAllUsers();
        User user = new User("g@mail.ru", "new login", "new name",
                LocalDate.of(1999, 8, 23));
        User user2 = new User("another", "another", "another",
                LocalDate.of(1999, 8, 23));
        userStorage.addUser(user);
        userStorage.addUser(user2);
        assertEquals(2, userStorage.getAllUsers().size());
        userStorage.deleteAllUsers();
    }

    @Test
    @Order(4)
    public void testDeleteUser() {
        User user = new User("g@mail.ru", "new login", "THE",
                LocalDate.of(1999, 8, 23));
        User result = userStorage.addUser(user);
        userStorage.deleteUser((int) result.getId());
        assertEquals(0, userStorage.getAllUsers().size());
        System.out.println("sddg");
        userStorage.deleteAllUsers();
    }

    @Test
    @Order(5)
    public void testChangeUser() {
        User user = new User("g@mail.ru", "login", "THE",
                LocalDate.of(1999, 8, 23));
        User newUser = new User("new@mail.ru", "new login", "new name",
                LocalDate.of(1999, 8, 23));
        long index = userStorage.addUser(user).getId();

        newUser.setId(index);
        assertEquals(newUser, userStorage.changeUSer(newUser));
    }

    @Test
    @Order(6)
    public void testAddFriendToUser() {
        //нужно сначала написить извлечение друзей
        User one = new User("g@mail.ru", "login", "one",
                LocalDate.of(1999, 8, 23));
        User two = new User("new@mail.ru", "new login", "two",
                LocalDate.of(1999, 8, 23));

        User indexOne = userStorage.addUser(one);
        User indexTwo = userStorage.addUser(two);
        userStorage.addFriendToUser(indexOne.getId(), indexTwo.getId());
        assertEquals(1, userStorage.getUserFriends(indexOne.getId()).size());
    }

    @Test
    @Order(7)
    public void testGetUserFriends() {
        //userStorage.deleteAllUsers();
        User one = new User("g@mail.ru", "login", "one",
                LocalDate.of(1999, 8, 23));
        User two = new User("new@mail.ru", "new login", "two",
                LocalDate.of(1999, 8, 23));
        User three = new User("new@mail.ru", "new login", "three",
                LocalDate.of(1999, 8, 23));
        User indexOne = userStorage.addUser(one);
        User indexTwo = userStorage.addUser(two);
        User indexThree = userStorage.addUser(three);

        userStorage.addFriendToUser(indexOne.getId(), indexTwo.getId());
        userStorage.addFriendToUser(indexOne.getId(), indexThree.getId());

        List<User> friends = new ArrayList<>(Arrays.asList(indexTwo, indexThree));
        assertEquals(friends, userStorage.getUserFriends(indexOne.getId()));
        userStorage.deleteAllUsers();
    }

    @Test
    @Order(8)
    public void testDeleteFromFriend() {
        User one = new User("g@mail.ru", "login", "one",
                LocalDate.of(1999, 8, 23));
        User two = new User("new@mail.ru", "new login", "two",
                LocalDate.of(1999, 8, 23));
        User indexOne = userStorage.addUser(one);
        User indexTwo = userStorage.addUser(two);
        userStorage.addFriendToUser(indexOne.getId(), indexTwo.getId());
        userStorage.deleteFromFriends(indexOne.getId(), indexTwo.getId());
        assertEquals(0, userStorage.getUserFriends(indexOne.getId()).size());
    }

    @Test
    @Order(9)
    public void tessst() {
        User one = new User("g@mail.ru", "login", "one",
                LocalDate.of(1999, 8, 23));
        System.out.println(one.getId());
        User result = userStorage.addUser(one);
        System.out.println(result.getId());
    }
}*/
