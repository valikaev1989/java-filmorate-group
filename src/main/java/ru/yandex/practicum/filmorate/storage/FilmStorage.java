package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    long addFilm(Film film);

    void changeFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(long id);

    List<Film> getSortFilmByDirectorSortByYear(Long directorId);

    List<Film> getSortFilmByDirectorSortByLikes(Long directorId);
    
    List<Film> getPopularFilmsSharedWithFriend(long userId, long friendId);

    List<Film> searchByDirectors(String query);

    List<Film> searchByTitles(String query);
}
