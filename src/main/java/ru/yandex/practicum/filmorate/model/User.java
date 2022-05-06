package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    int id;
    Set<Integer> friends = new HashSet<>();

    @Email(message = "Email has to be correct")
    String email;

    @NotBlank(message = "Login cannot be blank")
    @NotEmpty(message = "Login cannot be empty")
    String login;

    String name;

    @Past(message = "Birthday have to be in the past")
    LocalDate birthday;

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(int id) {
        friends.add(id);
    }

    public void deleteFriend(int id) {
        friends.remove(id);
    }
}
