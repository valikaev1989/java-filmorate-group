package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class MPA {
    private int id;
    @EqualsAndHashCode.Exclude
    private String name;

    public MPA(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
