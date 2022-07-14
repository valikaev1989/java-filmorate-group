package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ModelAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //++++++++++++++++++++++
    @Override
    public long addFilm(Film film) {
        List<Film> allFilms = getFilms();
        if (allFilms.contains(film)) {
            throw new ModelAlreadyExistException("Film already exist");
        } else {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film")
                    .usingGeneratedKeyColumns("film_id");
            return insert.executeAndReturnKey(film.toMap()).longValue();
        }
    }

    //+++++++++++++++++
    @Override
    public void changeFilm(Film film) {
        if (getFilms().stream().anyMatch(x -> x.getId() == film.getId())) {
            String sql = "update film set film_name = ?, description = ?, release_date = ?, duration_film = ?, mpa_id = ?" +
                    "where film_id = ?";
            jdbcTemplate.update(sql, film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
        } else {
            throw new ModelNotFoundException("Film not found with id " + film.getId());
        }
    }

    @Override
    public List<Film> getFilms() {
        String sql = "select * from film";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Film getFilmById(long id) {
        try {
            String sql = "select * from film where film_id = ?";
            return jdbcTemplate.queryForObject(sql, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ModelNotFoundException("Film wasn't found");
        }

    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int idFilm = resultSet.getInt("film_id");
        String name = resultSet.getString("film_name");
        String description = resultSet.getString("description");
        LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
        int duration = resultSet.getInt("duration_film");
        int rate = resultSet.getInt("rate");
        Film film = new Film(name, description, releaseDate, duration, rate);
        film.setId(idFilm);
        return film;
    }
}
