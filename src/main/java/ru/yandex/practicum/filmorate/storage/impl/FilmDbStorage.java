package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ModelAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

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

    @Override
    public void changeFilm(Film film) {
        String sql = "UPDATE FILM SET FILM_NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION_FILM = ?, MPA_ID = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * " +
                "FROM FILM F, MPA M " +
                "WHERE F.MPA_ID = M.MPA_ID";
        return jdbcTemplate.query(sql, FilmDbStorage::mapRowToFilm);
    }

    @Override
    public Film getFilmById(long id) {
        try {
            String sqlFilm = "SELECT * " +
                    "FROM FILM F, MPA M " +
                    "WHERE F.MPA_ID = M.MPA_ID AND FILM_ID = ?";
            return jdbcTemplate.queryForObject(sqlFilm, FilmDbStorage::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ModelNotFoundException("Film wasn't found");
        }
    }

    @Override
    public List<Film> getFilmsByDirectorSortedByYear(Long directorId) {
        String sqlQuery =
                "SELECT * " +
                        "FROM FILMS_DIRECTORS FD " +
                        "LEFT JOIN FILM F on  fd.FILM_ID=F.FILM_ID " +
                        "LEFT JOIN MPA M on F.MPA_ID = M.MPA_ID " +
                        "WHERE fd.director_id = ? " +
                        "ORDER BY RELEASE_DATE ASC";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::mapRowToFilm, directorId);
    }

    @Override
    public List<Film> getFilmsByDirectorSortedByLikes(Long directorId) {
        String sqlQuery =
                "SELECT * " +
                        "FROM FILMS_DIRECTORS FD " +
                        "LEFT JOIN FILM F ON  FD.FILM_ID=F.FILM_ID LEFT JOIN LIKES L ON F.FILM_ID = L.FILM_ID " +
                        "LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID WHERE FD.DIRECTOR_ID = ? " +
                        "GROUP BY F.FILM_ID " +
                        "ORDER BY COUNT(L.USER_ID) DESC";
        return jdbcTemplate.query(sqlQuery, FilmDbStorage::mapRowToFilm, directorId);
    }

    @Override
    public List<Film> getFilms(List<Long> ids) {
        String sqlQueryTemplate = "SELECT * " +
                                        "FROM FILM AS F " +
                                        "LEFT JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                                        "WHERE FILM_ID IN (%s)";
        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sqlQuery = String.format(sqlQueryTemplate, placeholders);
        return jdbcTemplate.query(sqlQuery, ids.toArray(), FilmDbStorage::mapRowToFilm);
    }

    public static Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        if (resultSet.getRow() != 0) {
            int idFilm = resultSet.getInt("film_id");
            String name = resultSet.getString("film_name");
            String description = resultSet.getString("description");
            LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
            int duration = resultSet.getInt("duration_film");
            int rate = resultSet.getInt("rate");
            Film film = new Film(name, description, releaseDate, duration, rate);
            film.setId(idFilm);
            film.setMpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name")));
            return film;
        } else {
            throw new ModelNotFoundException("MPA not found");
        }
    }

    public List<Film> getPopularFilmsSharedWithFriend(long userId, long friendId) {
        String sql = "SELECT * " +
                "FROM film f, MPA m " +
                "WHERE  f.MPA_ID = m.MPA_ID AND film_id IN (SELECT FILM_ID " +
                "                  FROM LIKES " +
                "                  WHERE film_id IN (SELECT film_id " +
                "                                    FROM likes " +
                "                                    WHERE user_id = ? " +
                "                                    INTERSECT " +
                "                                    SELECT film_id " +
                "                                    FROM likes " +
                "                                    WHERE user_id = ?) " +
                "                  GROUP BY FILM_ID " +
                "                  ORDER BY count(FILM_ID) DESC)";

        return jdbcTemplate.query(sql, FilmDbStorage::mapRowToFilm, userId, friendId);
    }

    public List<Film> getPopularFilmsByGenre(int limit, long genreId) {
        String sql = "SELECT * " +
                "FROM FILM F " +
                "         LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID " +
                "         LEFT JOIN LIKES L ON F.FILM_ID = L.FILM_ID " +
                "WHERE F.FILM_ID IN " +
                "                    (SELECT FILM_ID " +
                "                     FROM GENRE_AND_FILM " +
                "                     WHERE GENRE_ID = ?) " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY COUNT(L.USER_ID) DESC " +
                "LIMIT ?";

        return jdbcTemplate.query(sql, FilmDbStorage::mapRowToFilm, genreId, limit);
    }

    public List<Film> getPopularFilmsByYear(int limit, long year) {
        String sql = "SELECT * " +
                "FROM FILM F " +
                "         LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID " +
                "         LEFT JOIN LIKES L ON F.FILM_ID = L.FILM_ID " +
                "WHERE EXTRACT(YEAR FROM F.RELEASE_DATE) = ? " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY COUNT(L.USER_ID) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, FilmDbStorage::mapRowToFilm, year, limit);
    }

    public List<Film> getPopularFilmsByGenreAndYear(int limit, long genreId, long year) {
        String sql = "SELECT * " +
                "FROM FILM F " +
                "         LEFT JOIN LIKES L ON F.FILM_ID = L.FILM_ID " +
                "         LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID " +
                "         LEFT JOIN GENRE_AND_FILM G ON F.FILM_ID = G.FILM_ID " +
                "WHERE G.GENRE_ID = ? AND EXTRACT(YEAR FROM F.RELEASE_DATE) = ? " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY COUNT(L.USER_ID) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sql, FilmDbStorage::mapRowToFilm, genreId, year, limit);
    }

    @Override
    public List<Film> searchByDirectors(String query) {
        String sql =
                "SELECT * " +
                        "FROM FILM F " +
                        "LEFT JOIN FILMS_DIRECTORS FD ON F.FILM_ID = FD.FILM_ID " +
                        "LEFT JOIN DIRECTORS D ON FD.DIRECTOR_ID = D.DIRECTOR_ID " +
                        "LEFT JOIN LIKES L ON F.FILM_ID = L.FILM_ID " +
                        "LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID " +
                        "WHERE LOCATE(UPPER(?), UPPER(d.DIRECTOR_NAME)) " +
                        "ORDER BY F.RATE DESC";
        return jdbcTemplate.query(sql, FilmDbStorage::mapRowToFilm, query);
    }

    @Override
    public List<Film> searchByTitles(String query) {
        String sql =
                "SELECT * FROM film " +
                        "LEFT JOIN MPA M ON FILM.MPA_ID = M.MPA_ID " +
                        "WHERE LOCATE(UPPER(?), UPPER(description)) " +
                        "ORDER BY RATE DESC";
        return jdbcTemplate.query(sql, FilmDbStorage::mapRowToFilm, query);
    }

    @Override
    public void updateFilmRate(long filmId) {
        String sqlQuery =
                "UPDATE FILM F " +
                        "SET F.RATE = " +
                        "(SELECT COUNT(L.USER_ID) FROM LIKES L WHERE L.FILM_ID = ?) " +
                        "WHERE F.FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public boolean deleteFilm(long id) {
        String sql = "DELETE FROM FILM WHERE FILM_ID = ?";
        return jdbcTemplate.update(sql, id) > 0;
    }
}