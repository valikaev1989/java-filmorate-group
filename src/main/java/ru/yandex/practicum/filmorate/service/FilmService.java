package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Film changeFilm(Film film) {
        return filmStorage.changeFilm(film);
    }

    public void like(long id, long userId) {
        filmStorage.like(id, userId);
    }

    public void deleteLike(long id, long userId) {
        Film film = getFilmById(id);
        if(film.getLikes().contains(userId)) {
            filmStorage.deleteLike(id, userId);
        } else {
            throw new ModelNotFoundException("User not found with id " + userId);
        }

    }

    public List<Film> getPopularFilms(int count) {
        List<Film> allFilms = getFilms();
        allFilms.sort((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()));
        if(allFilms.size() <= count) {
            return allFilms;
        } else {
            return allFilms.subList(0, count);
        }
    }

    public MPA getMpaById(int id) {
        List<MPA> mpa = getAllMpa();
        if(mpa.stream().anyMatch(x -> x.getId() == id)) {
            return filmStorage.getMpaById(id);
        } else {
            throw new ModelNotFoundException("MPA not found with id " + id);
        }

    }

    public List<MPA> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public Genre getGenreById(int id) {
        List<Genre> genres = getAllGenres();
        if(genres.stream().anyMatch(x -> x.getId() == id)) {
            return filmStorage.getGenreById(id);
        } else {
            throw new ModelNotFoundException("Genre not found with id " + id);
        }

    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }
}
