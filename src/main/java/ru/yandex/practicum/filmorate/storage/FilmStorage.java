package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    void addFilm(Film film);

    void changeFilm(Film film);

    List<Film> getFilms();

    boolean isValid(Film film);

    void like(long id, long userId);

    void deleteLike(long id, long userId);

    Film getFilmById(long id);
}
