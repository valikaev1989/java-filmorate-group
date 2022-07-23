package ru.yandex.practicum.filmorate.storage.validators;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ModelAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

@Slf4j
@Component
public class DirectorValidate {
    private final JdbcTemplate jdbcTemplate;
    private static final String CHECK_ID_DIR = "SELECT * FROM DIRECTORS WHERE DIRECTOR_ID = ?";
    private static final String CHECK_DIR = "SELECT * FROM DIRECTORS WHERE DIRECTOR_ID = ? and DIRECTOR_NAME = ?";
    private static final String CHECK_ID_FILM = "SELECT * FROM FILM WHERE FILM_ID = ?";

    @Autowired
    public DirectorValidate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void validateIdDirector(Long directorId) {
        log.info("Проверка Id директора: " + directorId);
        SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(CHECK_ID_DIR, directorId);
        if (!sqlRow.next()) {
            log.error("директор с id: " + directorId + "отсутствует в БД");
            throw new ModelNotFoundException("Нет такого директора с id: " + directorId);
        }
    }

    public void validateExistFilm(Director director) {
        SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(CHECK_DIR, director.getId(), director.getName());
        if (sqlRow.next()) {
            log.error("Этот режиссер уже существует{}", director);
            throw new ModelAlreadyExistException("Этот пользователь уже существует");
        }
    }

    public void validateNameDirector(Director director) {
        if (director.getName() == null) {
            log.error("Имя директора не должен быть null");
            throw new ValidationException("имя директора null");
        }
        if (director.getName().isEmpty() || director.getName().equals(" ")) {
            log.error("Имя директора не должен быть пустым");
            throw new ValidationException("имя директора пустое");
        }if(director.getName().length()>50){
            log.error("Имя директора не должен быть более 50 символов");
            throw new ValidationException("имя директора длиннее 50 символов");
        }


    }

    public void validateNameAndExist(Director director) {
        validateExistFilm(director);
        validateNameDirector(director);
    }

    public void validateNameAndId(Director director) {
        validateIdDirector(director.getId());
        validateNameDirector(director);
    }

    public void validateFilmId(Long filmId) {
        if (filmId < 0) {
            throw new ModelNotFoundException(" filmId меньше нуля:" + filmId);
        }
        SqlRowSet sqlRow = jdbcTemplate.queryForRowSet(CHECK_ID_FILM, filmId);
        if (!sqlRow.next()) {
            throw new ValidationException("Нет такого фильма с filmId:" + filmId);
        }
    }
}
