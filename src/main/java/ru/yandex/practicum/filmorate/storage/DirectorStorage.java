package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

//TODO перед финальным ревью перенести на пакет выше
public interface DirectorStorage {
    Director addDirector(Director director);

    void addDirectorToFilm(Film film, long directorId);

    Director updateDirector(Director director);

    Director getDirector(Long directorId);

    void deleteDirector(Long directorId);

    void deleteDirectorFromFilm(Long filmId, Long directorId);

    List<Director> getAllDirectors();

    List<Director> getFilmDirectors(long filmId);

    void updateDirectorToFilm(Film film);
}