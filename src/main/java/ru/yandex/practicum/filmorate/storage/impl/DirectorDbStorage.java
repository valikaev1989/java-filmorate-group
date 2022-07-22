package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.validators.DirectorValidate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
public class DirectorDbStorage implements DirectorStorage {
    private final DirectorValidate directorValidate;
    private static final String GET_ALL_DIRECTORS = "SELECT * FROM DIRECTORS";
    private static final String UPDATE_DIRECTORS = "UPDATE DIRECTORS SET DIRECTOR_NAME = ? WHERE DIRECTOR_ID = ? ";
    private static final String GET_DIRECTOR_BY_ID = "SELECT * FROM DIRECTORS WHERE DIRECTOR_ID = ?";
    private static final String DELETE_DIRECTOR_BY_ID = "DELETE FROM DIRECTORS WHERE DIRECTOR_ID = ?";
    private static final String INSERT_DIR_TO_FILM = "INSERT INTO FILMS_DIRECTORS (FILM_ID, DIRECTOR_ID) VALUES (?, ?)";
    private static final String DELETE_DIRECTOR_FROM_FILM =
            "DELETE FROM FILMS_DIRECTORS WHERE FILM_ID = ? AND DIRECTOR_ID = ?";
    private static final String GET_DIRECTORS_FROM_FILM = "SELECT d.DIRECTOR_ID, d.DIRECTOR_NAME " +
            "FROM DIRECTORS AS d " +
            "LEFT JOIN FILMS_DIRECTORS AS fd ON d.DIRECTOR_ID = fd.DIRECTOR_ID WHERE fd.FILM_ID = ?";
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDbStorage(DirectorValidate directorValidate, JdbcTemplate jdbcTemplate) {
        this.directorValidate = directorValidate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Director> getAllDirectors() {
        log.info("Start DirectorStorage. Метод getAllDirectors.");
        return jdbcTemplate.query(GET_ALL_DIRECTORS, (this::mapRowToDirector));

    }

    @Override
    public Director addDirector(Director director) {
        directorValidate.validateNameAndExist(director);
        log.info("Start DirectorStorage. Метод addDirector. director:{}", director);
        Map<String, Object> keys = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingColumns("director_name")
                .usingGeneratedKeyColumns("director_id")
                .executeAndReturnKeyHolder(Map.of("director_name", director.getName()))
                .getKeys();
        director.setId((Long) ((Objects.requireNonNull(keys))).get("director_id"));
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        log.info("Start DirectorStorage. Метод updateDirector. director:{}", director);
        directorValidate.validateNameAndId(director);
        jdbcTemplate.update(UPDATE_DIRECTORS, director.getName(), director.getId());
        return getDirector(director.getId());
    }

    @Override
    public Director getDirector(Long directorId) {
        log.info("Start DirectorStorage. Метод getDirector. directorId:{}", directorId);
        directorValidate.validateIdDirector(directorId);
        SqlRowSet dirRows = jdbcTemplate.queryForRowSet(GET_DIRECTOR_BY_ID, directorId);
        if (dirRows.next()) {
            Director director = new Director(dirRows.getString("DIRECTOR_NAME"));
            director.setId(dirRows.getInt("DIRECTOR_ID"));
            return director;
        } else {
            throw new ModelNotFoundException("режиссера с Id:" + directorId + "нет");
        }

    }

    @Override
    public void deleteDirector(Long directorId) {
        directorValidate.validateIdDirector(directorId);
        log.info("удаление режиссера с directorId: {} ", directorId);
        jdbcTemplate.update(DELETE_DIRECTOR_BY_ID, directorId);
    }

    private Director mapRowToDirector(ResultSet result, int rowNum) throws SQLException {
        int id = result.getInt("director_id");
        String name = result.getString("director_name");
        return new Director(id, name);
    }

    @Override
    public void addDirectorToFilm(Film film, long directorId) {
        log.info("Start DirectorDbStorage addDirectorToFilm с directorId: {} в фильм {}", directorId, film);
        directorValidate.validateIdDirector(directorId);
        for (Director director : film.getDirectors()) {
            jdbcTemplate.update(INSERT_DIR_TO_FILM, film.getId(), director.getId());
        }

    }

    @Override
    public void deleteDirectorFromFilm(Long filmId, Long directorId) {
        log.info("Start DirectorDbStorage deleteDirectorFromFilm у filmId {} и directorId {}", filmId, directorId);
        directorValidate.validateIdDirector(directorId);
        directorValidate.validateFilmId(filmId);
        jdbcTemplate.update(DELETE_DIRECTOR_FROM_FILM, filmId, directorId);
    }

    @Override
    public List<Director> getFilmDirectors(long filmId) {
        log.info("Start DirectorDbStorage getFilmDirectors у filmId {}", filmId);
        directorValidate.validateFilmId(filmId);
        return jdbcTemplate.query(GET_DIRECTORS_FROM_FILM, this::mapRowToDirector, filmId);
    }
}
