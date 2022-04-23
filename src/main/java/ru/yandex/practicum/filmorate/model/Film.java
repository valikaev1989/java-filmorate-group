package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class Film {
    int id;

    @NotBlank(message = "Name cannot be blank")
    String name;

    @Size(min = 1, max = 200, message = "Description cannot be more then 200 symbol")
    String description;

    LocalDate releaseDate;

    @JsonProperty(value = "duration")
    @Positive
    Duration durationFilm;
}
