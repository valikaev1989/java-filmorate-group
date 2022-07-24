package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;
    private final DirectorService directorsStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       GenreStorage genreStorage,
                       LikesStorage likesStorage,
                       UserStorage userStorage,
                       DirectorService directorsStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.likesStorage = likesStorage;
        this.userStorage = userStorage;
        this.directorsStorage = directorsStorage;
    }

    //если б rate для этого создали, то на входи не поступали бы фильмы с rate = 4
    //и их не пришлось бы обнулять
    public Film addFilm(Film film) {
        long idFilm = filmStorage.addFilm(film);
        genreStorage.addGenresToFilm(film, idFilm);
        film.setId(idFilm);
        addDirectorInFilm(film);
        //likesStorage.updateRate(film.getId());
        return getFilmById(idFilm);
    }

    public Film getFilmById(long id) {
        Film film = filmStorage.getFilmById(id);
        film.setLikes(likesStorage.getLikes(id));
        film.setGenres(getGenresByFilmId(id));
        film.setDirectors(directorsStorage.getDirectorsFromFilm(id));
        return film;
    }

    private Set<Genre> getGenresByFilmId(long filmId) {
        return new HashSet<>(genreStorage.getFilmGenres(filmId));
    }

    public Film changeFilm(Film film) {
        if (getFilms().stream().anyMatch(x -> x.getId() == film.getId())) {
            filmStorage.changeFilm(film);
            genreStorage.changeFilmGenres(film);
            directorsStorage.updateDirectorToFilm(film);
        } else {
            throw new ModelNotFoundException("Film not found with id " + film.getId());
        }
        return film;
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
        for (Film film : films) {
            film.setGenres(getGenresByFilmId(film.getId()));
            film.setLikes(likesStorage.getLikes(film.getId()));
            film.setDirectors(directorsStorage.getDirectorsFromFilm(film.getId()));
        }
        return films;
    }

    public List<Film> getPopularFilms(int count) {
        return likesStorage.getPopularFilms(count);
    }

    public boolean checkDate(Film film) {
        return film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 28));
    }

    public void deleteDirectorInFilm(long filmId, long directorId) {
        log.info("Start filmService. Метод deleteDirectorInFilm. directorId:{},  filmId{}.", directorId, filmId);
        directorsStorage.deleteDirectorFromFilm(filmId, directorId);
    }

    public void addDirectorInFilm(Film film) {
        log.info("Start filmService. Метод addDirectorInFilm. film:{}.", film);
        List<Director> directors = film.getDirectors();
        for (Director director : directors) {
            directorsStorage.addDirectorToFilm(film, director.getId());
        }
    }

    public List<Film> getSortFilmByDirector(Long directorId, String sortBy) {
        log.info("Start filmService. Метод getSortFilmByDirector. directorId:{}, parameter:{}.", directorId, sortBy);
        directorsStorage.getDirector(directorId);
        List<Film> films;
        switch (sortBy) {
            case "year":
                films = filmStorage.getSortFilmByDirectorSortByYear(directorId);
                break;
            case "likes":
                films = filmStorage.getSortFilmByDirectorSortByLikes(directorId);
                break;
            default:
                throw new ModelNotFoundException("сортировка не верна");
        }
        for (Film film : films) {
            film.setGenres(getGenresByFilmId(film.getId()));
            film.setLikes(likesStorage.getLikes(film.getId()));
            film.setDirectors(directorsStorage.getDirectorsFromFilm(film.getId()));
        }
        return films;
    }
    
    public List<Film> getPopularFilmsSharedWithFriend(long userId, long friendId) {
        return filmStorage.getPopularFilmsSharedWithFriend(userId, friendId);
    }

    public List<Film> getPopularFilmsSearchByDirectorTitle(String query, List<String> by) {
        List<Film> films = new ArrayList<>();
        for (String sortBy : by) {
            switch (sortBy) {
                case "title":
                    films.addAll(filmStorage.searchByTitles(query));
                    break;
                case "director":
                    films.addAll(filmStorage.searchByDirectors(query));
                    break;
                default:
                    throw new ValidationException("Bad search arg");
            }
        }
        for (Film film : films) {
            film.setGenres(getGenresByFilmId(film.getId()));
            film.setLikes(likesStorage.getLikes(film.getId()));
            film.setDirectors(directorsStorage.getDirectorsFromFilm(film.getId()));
        }
        return films;
    }
}
