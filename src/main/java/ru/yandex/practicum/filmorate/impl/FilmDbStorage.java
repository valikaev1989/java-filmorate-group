package ru.yandex.practicum.filmorate.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ModelAlreadyExistException;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        List<Film> allFilms = getFilms();
         if (allFilms.contains(film)) {
            throw new ModelAlreadyExistException("Film already exist");
        } else {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("film")
                    .usingGeneratedKeyColumns("film_id");
            long idFilm = insert.executeAndReturnKey(film.toMap()).longValue();
            addGenres(film, idFilm);
            return getFilmById(idFilm);
        }
    }

    @Override
    public Film changeFilm(Film film) {
        if (getFilms().stream().anyMatch(x -> x.getId() == film.getId())) {
            String sql = "update film set film_name = ?, description = ?, release_date = ?, duration_film = ?, mpa_id = ?" +
                    "where film_id = ?";
            jdbcTemplate.update(sql, film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId());
            changeGenre(film);
            return getFilmById(film.getId());
        } else {
            throw new ModelNotFoundException("Film not found with id " + film.getId());
        }
    }

    private void changeGenre(Film film) {
        if(film.getGenres().size() != 0) {
            deleteGenre(film);
            addGenres(film, film.getId());
        } else {
            deleteGenre(film);
        }
    }

    private void addGenres(Film film, long idFilm) {
        String sql = "INSERT INTO genre_and_film (genre_id, film_id) " +
                "VALUES (?, ?)";
        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            for (Genre genre : genres) {
                jdbcTemplate.update(sql, genre.getId(), idFilm);
            }
        }
    }

    private void deleteGenre(Film film) {
        String sql = "DELETE FROM genre_and_film WHERE film_id = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    @Override
    public List<Film> getFilms() {
        String sql = "select * from film";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public boolean isValid(Film film) {
        return false;
    }

    @Override
    public void like(long id, long userId) {
        String sql = "insert into likes(film_id, user_id)" +
                "values(?, ?)";
        jdbcTemplate.update(sql, id, userId);
    }

    @Override
    public void deleteLike(long id, long userId) {
        String sql = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, id, userId);
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

    @Override
    public MPA getMpaById(int id) {
        String sql = "SELECT * " +
                "FROM mpa " +
                "WHERE mpa_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, id);
    }

    @Override
    public List<MPA> getAllMpa() {
        String sql = "SELECT * FROM mpa";
        return jdbcTemplate.query(sql, this::mapRowToMpa);
    }

    @Override
    public Genre getGenreById(int id) {
        String sql = "SELECT * " +
                "FROM genre " +
                "WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToGenre, id);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genre";
        return jdbcTemplate.query(sql, this::mapRowToGenre);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        int idFilm = resultSet.getInt("film_id");
        String name = resultSet.getString("film_name");
        String description = resultSet.getString("description");
        LocalDate releaseDate = resultSet.getDate("release_date").toLocalDate();
        int duration = resultSet.getInt("duration_film");
        int rate = resultSet.getInt("rate");
        int idMpa = resultSet.getInt("mpa_id");
        MPA mpa = getMpa(idMpa);
        Set<Genre> genres = new HashSet<>(getGenres(idFilm));
        Set<Long> likes = new HashSet<>(getLikes(idFilm));
        Film film = new Film(name, description, releaseDate, duration, rate);
        film.setLikes(likes);
        film.setId(idFilm);
        film.setMpa(mpa);
        film.setGenres(genres);
        return film;
    }

    private MPA getMpa(long idMpa) {
        String sql = "SELECT mpa_id, mpa_name FROM mpa WHERE mpa_id = ?";
        return jdbcTemplate.queryForObject(sql, this::mapRowToMpa, idMpa);
    }

    private MPA mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        int idMpa = resultSet.getInt("mpa_id");
        String name = resultSet.getString("mpa_name");
        return new MPA(idMpa, name);
    }

    private List<Genre> getGenres(long idFilm) {
        String sql = "SELECT g.genre_id, g.name " +
                "FROM genre_and_film AS gaf " +
                "LEFT JOIN genre AS g ON gaf.genre_id = g.genre_id " +
                "WHERE film_id = ? ";
        return jdbcTemplate.query(sql, this::mapRowToGenre, idFilm);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        int idGenre = resultSet.getInt("genre_id");
        String name = resultSet.getString("name");
        return new Genre(idGenre, name);
    }

    private List<Long> getLikes(long idFilm) {
        String sql = "SELECT user_id " +
                "FROM likes " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToLong, idFilm);
    }

    private long mapRowToLong(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("user_id");
    }
}
