package ru.yandex.practicum.filmorate.exceptions;

public class ModelAlreadyExistException extends RuntimeException {
    public ModelAlreadyExistException(String message) {
        super(message);
    }
}
