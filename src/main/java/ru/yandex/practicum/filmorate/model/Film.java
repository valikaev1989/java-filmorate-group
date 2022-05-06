package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    int id;
    Set<Integer> likes = new HashSet<>();

    @NotBlank(message = "Name cannot be blank")
    String name;

    @Size(min = 1, max = 200, message = "Description cannot be more then 200 symbol")
    String description;

    LocalDate releaseDate;

    @JsonProperty(value = "duration")
    @Positive
    Duration durationFilm;

    public Film(int id, String name, String description, LocalDate releaseDate, Duration durationFilm) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.durationFilm = durationFilm;
    }

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void deleteLike(int userId) {
        likes.remove(userId);
    }
}
