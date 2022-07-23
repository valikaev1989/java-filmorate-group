package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventType;
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
    private final EventsStorage eventsStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, GenreStorage genreStorage,
                       LikesStorage likesStorage, EventsStorage eventsStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.likesStorage = likesStorage;
        this.eventsStorage = eventsStorage;
    }

    //если б rate для этого создали, то на входи не поступали бы фильмы с rate = 4
    //и их не пришлось бы обнулять
    public Film addFilm(Film film) {
        long idFilm = filmStorage.addFilm(film);
        genreStorage.addGenresToFilm(film, idFilm);
        //likesStorage.updateRate(film.getId());
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

    public void like(long filmId, long userId) {
        likesStorage.like(filmId, userId);
        eventsStorage.addEvent(userId, filmId, EventType.LIKE, EventOperations.ADD);
    }

    public void deleteLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        if (film.getLikes().contains(userId)) {
            likesStorage.deleteLike(filmId, userId);
            eventsStorage.addEvent(userId, filmId, EventType.LIKE, EventOperations.REMOVE);
        } else {
            throw new ModelNotFoundException("User not found with id " + userId);
        }
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        for (Film film : films) {
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
