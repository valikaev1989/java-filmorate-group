package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.impl.DirectorDbStorage;
import ru.yandex.practicum.filmorate.storage.validators.DirectorValidate;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorService {
    private final DirectorDbStorage directorDbStorage;
    private final DirectorValidate directorValidate;

    @Autowired
    public DirectorService(DirectorDbStorage directorDbStorage, DirectorValidate directorValidate) {
        this.directorDbStorage = directorDbStorage;
        this.directorValidate = directorValidate;
    }

    public List<Director> getAllDirectors() {
        return directorDbStorage.getAllDirectors();
    }

    public Director getDirector(Long directorId) {
        directorValidate.validateIdDirector(directorId);
        Optional<Director> director = directorDbStorage.getDirector(directorId);
        if (director.isPresent()) {
            return director.get();
        } else {
            throw new ModelNotFoundException(String.format("Not found director %s", directorId));
        }
    }

    public Director addDirector(Director director) {
        directorValidate.validateNameAndExist(director);
        return directorDbStorage.addDirector(director);
    }

    public Director updateDirector(Director director) {
        directorValidate.validateNameAndId(director);
        return directorDbStorage.updateDirector(director);
    }

    public void deleteDirector(Long directorId) {
        directorValidate.validateIdDirector(directorId);
        directorDbStorage.deleteDirector(directorId);
    }

    public void deleteDirectorFromFilm(Long filmId, Long directorId) {
        directorValidate.validateIdDirector(directorId);
        directorValidate.validateFilmId(filmId);
        directorDbStorage.deleteDirectorFromFilm(filmId, directorId);
    }

    //todo валидация фильма?
    public void addDirectorToFilm(Film film, Long directorId) {
        directorValidate.validateIdDirector(directorId);
        directorDbStorage.addDirectorToFilm(film, directorId);
    }

    public void updateDirectorToFilm(Film film) {
        directorDbStorage.updateDirectorToFilm(film);
    }

    public List<Director> getDirectorsByFilm(Long filmId) {
        directorValidate.validateFilmId(filmId);
        return directorDbStorage.getDirectorsByFilm(filmId);
    }
}