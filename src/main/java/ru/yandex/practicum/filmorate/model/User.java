package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class User {
    int id;

    @Email(message = "Email has to be correct")
    String email;

    @NotBlank(message = "Login cannot be blank")
    @NotEmpty(message = "Login cannot be empty")
    String login;

    String name;

    @Past(message = "Birthday have to be in the past")
    LocalDate birthday;
}
