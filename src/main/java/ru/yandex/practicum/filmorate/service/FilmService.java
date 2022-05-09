package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addFilm(Film film) {
        filmStorage.addFilm(film);
    }

    public void changeFilm(Film film) {
        filmStorage.changeFilm(film);
    }

    public void like(long id, long userId) {
        filmStorage.like(id, userId);
    }

    public void deleteLike(long id, long userId) {
        filmStorage.deleteLike(id, userId);
    }

    public List<Film> getPopularFilms(int count) {
        List<Film> allFilms = getFilms();
        allFilms.sort((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()));
        //allFilms.sort(Comparator.comparingInt(o -> o.getLikes().size()));
        if(allFilms.size() <= count) {
            return allFilms;
        } else {
            return allFilms.subList(0, count);
        }
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }
}
