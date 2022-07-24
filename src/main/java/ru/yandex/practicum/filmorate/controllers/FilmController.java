package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Get all films");
        return filmService.getFilms();
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (filmService.checkDate(film)) {
            log.info(String.format("Film %s was added", film.getName()));
            return filmService.addFilm(film);
        } else {
            throw new ValidationException("Wrong release date");
        }
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info(String.format("Get film %d", id));
        return filmService.getFilmById(id);
    }

    @PutMapping("/films")
    public Film changeFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (filmService.checkDate(film)) {
            log.info(String.format("Film %d was changed", film.getId()));
            return filmService.changeFilm(film);
        } else {
            throw new ValidationException("Wrong release date");
        }
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void like(@PathVariable long id, @PathVariable long userId) {
        log.info(String.format("User %d likes film %d", userId, id));
        filmService.like(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info(String.format("User %d deleted like from film %d", userId, id));
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Get popular films");
        return filmService.getPopularFilms(count);
    }

    @DeleteMapping("/films/{id}")
    public void deleteFilmById(@PathVariable long id) {
        log.info("Delete film {}", id);
        filmService.deleteFilm(id);
    }


    @GetMapping("/films/director/{directorId}")
    public List<Film> getSortFilmByDirector(@PathVariable long directorId,
                                            @RequestParam String sortBy) {
        log.info("Получен запрос к эндпоинту /films/director/{id}. Метод GET");
        return filmService.getSortFilmByDirector(directorId, sortBy);
    }

    @DeleteMapping("/films{filmId}/directors/{directorId}")
    public void deleteDirectorsFromFilm(@PathVariable long filmId, @PathVariable long directorId) {
        log.info("Получен запрос к эндпоинту /films{id}/director/{id}. Метод DELETE");
        filmService.deleteDirectorInFilm(filmId, directorId);
    }

    /**
     * Возвращает список общих с другом фильмов с сортировкой по их популярности.
     * API: GET /films/common?userId={userId}&friendId={friendId}
     *
     * @param userId   идентификатор пользователя, запрашивающего информацию
     * @param friendId идентификатор пользователя, с которым необходимо сравнить список фильмов
     * @return Возвращает список фильмов, отсортированных по популярности.
     */
    @GetMapping("/films/common")
    public List<Film> getPopularFilmsSharedWithFriend(@RequestParam long userId, @RequestParam long friendId) {
        log.info("Get popular films shared with a friend.");
        return filmService.getPopularFilmsSharedWithFriend(userId, friendId);
    }
}