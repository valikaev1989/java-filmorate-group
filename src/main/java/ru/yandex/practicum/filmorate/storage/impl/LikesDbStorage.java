package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
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
    public void like(long filmId, long userId) {
        String sql = "INSERT INTO likes(film_id, user_id)" +
                "VALUES(?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public Set<Long> getLikes(long filmId) {
        String sql = "SELECT user_id " +
                "FROM likes " +
                "WHERE film_id = ?";
        List<Long> likes = jdbcTemplate.query(sql, this::mapRowToLong, filmId);
        return new HashSet<>(likes);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "select f.FILM_ID, f.RELEASE_DATE, f.RATE, f.DESCRIPTION, f.DURATION_FILM, f.FILM_NAME, f.MPA_ID, count(USER_ID) " +
                "from FILM as f " +
                "left join LIKES L on F.FILM_ID = L.FILM_ID " +
                "group by f.FILM_ID, f.RELEASE_DATE, f.RATE, f.DESCRIPTION, f.DURATION_FILM, f.FILM_NAME, f.MPA_ID " +
                "order by count(l.USER_ID) desc " +
                "limit ?";
        return jdbcTemplate.query(sql,
                (rs, numRow) ->
                        new Film(
                                rs.getLong("film_id"),
                                rs.getString("film_name"),
                                rs.getString("description"),
                                rs.getDate("release_date").toLocalDate(),
                                rs.getInt("duration_film"),
                                rs.getInt("rate"),
                                getMpa(rs.getLong("film_id"))
                        ), count
                );
    }

    private Mpa getMpa(long filmId){
        String sql = "select * " +
                "from mpa " +
                "left join FILM F on MPA.MPA_ID = F.MPA_ID " +
                "where film_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{filmId}, (rs, rowNum) ->
                new Mpa(
                        rs.getInt("mpa_id"),
                        rs.getString("mpa_name")
                        )

        );
    }

    private long mapRowToLong(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("user_id");
    }
}
