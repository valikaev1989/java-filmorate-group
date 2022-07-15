package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       GenreStorage genreStorage,
                       LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.likesStorage = likesStorage;
    }

    public Film addFilm(Film film) {
        long idFilm = filmStorage.addFilm(film);
        genreStorage.addGenresToFilm(film, idFilm);
        return getFilmById(idFilm);
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id);
        film.setLikes(likesStorage.getLikes(id));
        film.setGenres(getGenresByFilmId(id));
        return film;
    }

    private Set<Genre> getGenresByFilmId(long filmId) {
        return new HashSet<>(genreStorage.getFilmGenres(filmId));
    }

    public Film changeFilm(Film film) {
        if (getFilms().stream().anyMatch(x -> x.getId() == film.getId())) {
            filmStorage.changeFilm(film);
            genreStorage.changeFilmGenres(film);
        } else {
            throw new ModelNotFoundException("Film not found with id " + film.getId());
        }
        return getFilmById(film.getId());
    }
    public void like(long id, long userId) {
        likesStorage.like(id, userId);
    }

    public void deleteLike(long id, long userId) {
        Film film = getFilmById(id);
        if (film.getLikes().contains(userId)) {
            likesStorage.deleteLike(id, userId);
        } else {
            throw new ModelNotFoundException("User not found with id " + userId);
        }
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        for(Film film : films) {
            film.setGenres(getGenresByFilmId(film.getId()));
            film.setLikes(likesStorage.getLikes(film.getId()));
        }
        return films;
    }

    public List<Film> getPopularFilms(int count) {
        return likesStorage.getPopularFilms(count);
    }

    public boolean checkDate(Film film) {
        return film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28));
    }
}
