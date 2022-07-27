package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
public class LikesDbStorage implements LikesStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void like(long id, long userId) {
        String sql = "INSERT INTO LIKES(FILM_ID, USER_ID)" +
                "VALUES(?, ?)";
        jdbcTemplate.update(sql, id, userId);
        updateRate(id);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sql = "DELETE FROM LIKES " +
                "WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public Set<Long> getLikes(long filmId) {
        String sql = "SELECT USER_ID " +
                "FROM LIKES " +
                "WHERE FILM_ID = ?";
        List<Long> likes = jdbcTemplate.query(sql, LikesDbStorage::mapRowToLong, filmId);
        return new HashSet<>(likes);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "SELECT * FROM FILM F " +
                "LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID " +
                "ORDER BY F.RATE DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, FilmDbStorage::mapRowToFilm, count);
    }

    private static long mapRowToLong(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("user_id");
    }

    @Override
    public void updateRate(long filmId) {
        String sql = "UPDATE FILM F SET RATE = (SELECT COUNT(L.USER_ID) FROM LIKES L WHERE L.FILM_ID = F.FILM_ID) " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, filmId);
    }

    @Override
    public List<Long> getRecommendations(long userId) {
        String sqlQuery =
                "SELECT FILM_ID FROM LIKES WHERE USER_ID = " +
                        "(SELECT USER_ID FROM LIKES WHERE FILM_ID IN " +
                        "(SELECT FILM_ID FROM LIKES WHERE USER_ID = ?) AND USER_ID <> ? " +
                        "GROUP BY USER_ID ORDER BY COUNT(FILM_ID) DESC LIMIT 1) " +
                        "AND FILM_ID NOT IN (SELECT FILM_ID FROM LIKES WHERE USER_ID = ?)";
        return jdbcTemplate.query(sqlQuery, this::makeFilmId, userId, userId, userId);
    }

    private long makeFilmId(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong("film_id");
    }
}