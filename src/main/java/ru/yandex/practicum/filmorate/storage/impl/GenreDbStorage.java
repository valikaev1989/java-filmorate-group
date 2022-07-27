package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreById(int id) {
        String sql = "SELECT * " +
                "FROM GENRE " +
                "WHERE GENRE_ID = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM GENRE";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    @Override
    public List<Genre> getFilmGenres(long idFilm) {
        String sql = "SELECT G.GENRE_ID, G.NAME " +
                "FROM GENRE AS G " +
                "LEFT JOIN GENRE_AND_FILM AS GAF ON G.GENRE_ID = GAF.GENRE_ID " +
                "WHERE GAF.FILM_ID = ?";
        return jdbcTemplate.query(sql, this::mapRowToGenre, idFilm);
    }

    @Override
    public void addGenresToFilm(Film film, long idFilm) {
        String sql = "INSERT INTO GENRE_AND_FILM (GENRE_ID, FILM_ID) " +
                "VALUES (?, ?)";
        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            for (Genre genre : genres) {
                jdbcTemplate.update(sql, genre.getId(), idFilm);
            }
        }
    }

    @Override
    public void deleteFilmAllGenres(long idFilm) {
        String sql = "DELETE FROM GENRE_AND_FILM " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, idFilm);
    }

    @Override
    public void changeFilmGenres(Film film) {
        if (film.getGenres().size() != 0) {
            deleteFilmAllGenres(film.getId());
            addGenresToFilm(film, film.getId());
        } else {
            deleteFilmAllGenres(film.getId());
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int i) throws SQLException {
        int idGenre = resultSet.getInt("genre_id");
        String name = resultSet.getString("name");
        return new Genre(idGenre, name);
    }
}