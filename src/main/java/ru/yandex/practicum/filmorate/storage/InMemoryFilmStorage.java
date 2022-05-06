package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.CreatorId;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Integer, Film> allFilms = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(allFilms.values());
    }

    @Override
    public void addFilm(Film film) {
        if (isValid(film)) {
            film.setId(CreatorId.createFilmId());
            allFilms.put(film.getId(), film);
        }
    }

    @Override
    public void changeFilm(Film film) {
        if (isValid(film)) {
            if (allFilms.containsKey(film.getId())) {
                allFilms.put(film.getId(), film);
            } else {
                addFilm(film);
            }
        }
    }

    @Override
    public boolean isValid(Film film) {
        if (film.getName().isEmpty() | film.getName().isBlank()) {
            log.warn("Название фильма пустое");
            throw new ValidationException("Название фильма не может быть пустым");
        } else if(film.getDescription().isEmpty() | film.getDescription().isBlank()) {
            log.warn("Описание фильма пустое");
            throw new ValidationException("Описание фильма не может быть пустым");
        }else if (film.getDescription().length() > 200) {
            log.warn("Описание фильма " + film.getName() + " больше 200 знаков");
            throw new ValidationException("Описание не может превышать 200 знаков");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Дата выпуска фильма " + film.getName() + " раньше 28.12.1895");
            throw new ValidationException("Дата выпуска фильма не может быть раньше 28.12.1895");
        } else if (film.getDurationFilm().isNegative()) {
            log.warn("Продолжительность фильма " + film.getName() + " отрицательная");
            throw new ValidationException("Продолжительность фильма не может быть отрицательной");
        } else {
            return true;
        }
    }

    @Override
    public void like(int id, int userId) {
        allFilms.get(id).addLike(userId);
    }

    @Override
    public void deleteLike(int id, int userId) {
        if(allFilms.containsKey(id) && allFilms.containsKey(userId)) {
            allFilms.get(id).deleteLike(userId);
        } else {
            throw new FilmNotFoundException("Film id not found " + id, userId);
        }
    }

    @Override
    public Film getFilmById(int id) {
        if(allFilms.containsKey(id)) {
            return allFilms.get(id);
        } else {
            throw new FilmNotFoundException("Film not found id ", id);
        }
    }
}
