package ru.yandex.practicum.filmorate.exceptions;

public class FilmNotFoundException extends RuntimeException {
    int id;

    public FilmNotFoundException(String message, int id) {
        super(message + " id " + id);
    }
}
