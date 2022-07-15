package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class MpaDbStorage implements MpaStorage {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Mpa getMpaById(int id) {
        String sql = "SELECT * " +
                "FROM mpa " +
                "WHERE mpa_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Mpa getMpaByFilmId(long filmId) {
        String sql = "SELECT mpa.mpa_id, mpa.mpa_name " +
                "FROM mpa " +
                "LEFT JOIN film ON mpa.mpa_id = film.mpa_id " +
                "WHERE film.film_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, filmId);
    }


    private Mpa mapRowToMpa(ResultSet resultSet, int i) throws SQLException {
        int idMpa = resultSet.getInt("mpa_id");
        String name = resultSet.getString("mpa_name");
        return new Mpa(idMpa, name);
    }
}
