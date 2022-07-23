package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventType;
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
        String sql = "INSERT INTO likes(film_id, user_id)" +
                "VALUES(?, ?)";
        jdbcTemplate.update(sql, id, userId);
        updateRate(id);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sql = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sql, filmId, userId);
        updateRate(filmId);
    }

    @Override
    public Set<Long> getLikes(long filmId) {
        String sql = "SELECT user_id " +
                "FROM likes " +
                "WHERE film_id = ?";
        List<Long> likes = jdbcTemplate.query(sql, LikesDbStorage::mapRowToLong, filmId);
        return new HashSet<>(likes);
    }

    //именно из за этой закомментированной строки ниже и получилось что у тебя тесты не падали на выводе пополярных фильмов
    @Override
    public List<Film> getPopularFilms(int count) {
        String sql = "select f.FILM_ID, f.RELEASE_DATE, f.RATE, f.DESCRIPTION, f.DURATION_FILM, f.FILM_NAME, MPA.MPA_ID, MPA.MPA_NAME, count(USER_ID) " +
                "from FILM as f " +
                "left join LIKES L on F.FILM_ID = L.FILM_ID " +
                "left join MPA on f.MPA_ID = MPA.MPA_ID " +
                "group by f.FILM_ID, f.RELEASE_DATE, f.RATE, f.DESCRIPTION, f.DURATION_FILM, f.FILM_NAME, f.MPA_ID, MPA.MPA_NAME " +
                "order by count(l.USER_ID) desc " +
                "limit ?";
        //String sql = "select * from FILM f, MPA m where f.MPA_ID = m.MPA_ID order by RATE desc limit ?";
        return jdbcTemplate.query(sql, FilmDbStorage::mapRowToFilm, count);
    }

    private static long mapRowToLong(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("user_id");
    }

    public void updateRate(long filmId) {
        String sql = "update FILM f set rate = (select count(l.user_id) " +
                "from LIKES l where l.film_id = f.film_id) where film_id = ?";
        jdbcTemplate.update(sql, filmId);
    }
}
