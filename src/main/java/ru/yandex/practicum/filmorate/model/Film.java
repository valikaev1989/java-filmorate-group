package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class Film {
    @EqualsAndHashCode.Exclude
    private long id;
    private Set<Long> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Size(min = 1, max = 200, message = "Description cannot be more then 200 symbol")
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Min(1)
    private int duration;

    @NotNull
    private Mpa mpa;

    private int rate;

    public Film() {}

    public Film(String name, String description, LocalDate releaseDate, int duration, int rate) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }

    public Film(Set<Long> likes, Set<Genre> genres, String name, String description, LocalDate releaseDate, int duration, Mpa mpa, int rate) {
        this.likes = likes;
        this.genres = genres;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = rate;
    }

    public void addLike(long userId) {
        likes.add(userId);
    }

    public void deleteLike(long userId) {
        likes.remove(userId);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("film_name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration_film", duration);
        values.put("rate", rate);
        values.put("mpa_id", mpa.getId());

        return values;
    }
}
