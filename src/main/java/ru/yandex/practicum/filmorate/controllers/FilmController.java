package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping()
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        filmService.addFilm(film);
        return film;
    }

    @PutMapping()
    public Film changeFilm(@RequestBody Film film) throws ValidationException {
        filmService.changeFilm(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void like(@PathVariable int id, @PathVariable int userId) {
        filmService.like(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }
}
