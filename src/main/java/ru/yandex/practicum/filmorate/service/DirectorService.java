package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.impl.DirectorDbStorage;

import java.util.List;

@Slf4j
@Service
public class DirectorService {
    private final DirectorDbStorage directorDbStorage;

    @Autowired
    public DirectorService(DirectorDbStorage directorDbStorage) {
        this.directorDbStorage = directorDbStorage;
    }

    public List<Director> getAllDirectors() {
        return directorDbStorage.getAllDirectors();
    }

    public Director getDirector(Long directorId) {
        return directorDbStorage.getDirector(directorId);
    }

    public Director addDirector(Director director) {
        return directorDbStorage.addDirector(director);
    }

    public Director updateDirector(Director director) {
        return directorDbStorage.updateDirector(director);
    }

    public void deleteDirector(Long directorId) {
        directorDbStorage.deleteDirector(directorId);
    }

    public void deleteDirectorFromFilm(Long filmId, Long directorId) {
        directorDbStorage.deleteDirectorFromFilm(filmId, directorId);
    }

    public void addDirectorToFilm(Film film, Long directorId) {
        directorDbStorage.addDirectorToFilm(film, directorId);
    }
    public List<Director> getDirectorsFromFilm(Long filmId){
        return directorDbStorage.getFilmDirectors(filmId);
    }
}
