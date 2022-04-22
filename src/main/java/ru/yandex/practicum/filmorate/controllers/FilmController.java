package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private HashMap<Integer, Film> allFilms = new HashMap<>();

    @PostMapping()
    public void addFilm(@RequestBody Film film) throws ValidationException {
        if (film.getName().isEmpty() | film.getName().isBlank()) {
            log.warn("Название фильма пустое");
            throw new ValidationException("Название фильма не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            log.warn("Описание фильма " + film.getName() + " больше 200 знаков");
            throw new ValidationException("Описание не может превышать 200 знаков");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата выпуска фильма " + film.getName() + " раньше 28.12.1895");
            throw new ValidationException("Дата выпуска фильма не может быть раньше 28.12.1895");
        } else if (film.getDurationFilm().isNegative()) {
            log.warn("Продолжительность фильма " + film.getName() + " отрицательная");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        } else {
            allFilms.put(film.getId(), film);
            log.info("Добавлен фильм " + film.getName());
        }
    }

    //я оставила такую же реализацию как в добавлении фильма, потому что фильм либо автоматом добавляется,
    //либо изменяется, все фильмы же хранятся в хэш-мапе
    @PutMapping()
    public void changeFilm(@RequestBody Film film) throws ValidationException {
        addFilm(film);
    }

    @GetMapping()
    public List<Film> getFilms() {
        List<Film> result = new ArrayList<>();
        for (Film film : allFilms.values()) {
            result.add(film);
        }
        return result;
    }
}
