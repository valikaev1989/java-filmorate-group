package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film changeFilm(Film film);

    List<Film> getFilms();

    boolean isValid(Film film);

    void like(long id, long userId);

    void deleteLike(long id, long userId);

    Film getFilmById(long id);

    MPA getMpaById(int id);

    List<MPA> getAllMpa();

    Genre getGenreById(int id);

    List<Genre> getAllGenres();
}
