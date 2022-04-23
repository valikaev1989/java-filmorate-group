package ru.yandex.practicum.filmorate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }
}
