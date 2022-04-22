package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class Film {
    int id;
    String name;
    String description;
    LocalDate releaseDate;
    @JsonProperty(value = "duration")
    Duration durationFilm;
}
