package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Genre {
    int id;
    @EqualsAndHashCode.Exclude
    String name;

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
