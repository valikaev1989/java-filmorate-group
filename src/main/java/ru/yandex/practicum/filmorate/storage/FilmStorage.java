package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    long addFilm(Film film);

    void changeFilm(Film film);

    List<Film> getFilms();

    Film getFilmById(long id);

    List<Film> getSortFilmByDirectorSortByYear(Long directorId);

    List<Film> getSortFilmByDirectorSortByLikes(Long directorId);
    
    List<Film> getPopularFilmsSharedWithFriend(long userId, long friendId);

    List<Film>  getPopularFilmsByGenre(int limit, long genreId);

    List<Film>  getPopularFilmsByYear(int limit, long year);

    List<Film> getPopularFilmsByGenreAndYear(int limit, long genreId, long year);
}
