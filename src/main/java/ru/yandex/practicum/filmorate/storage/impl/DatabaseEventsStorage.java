package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.storage.EventsStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseEventsStorage implements EventsStorage {
    private static final String GET_EVENTS = "SELECT E.EVENT_ID, E.TIME_STAMP, E.USER_ID, E.EVENT_TYPE, " +
            "E.EVENT_OPERATION, E.ENTITY_ID " +
            "FROM EVENTS E " +
            "JOIN USERR U ON E.USER_ID = U.USER_ID " +
            "JOIN FRIENDS F ON U.USER_ID = F.FRIEND_ID " +
            "WHERE F.USER_ID  = ?";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getEvents(Long id) {
        List<Event> list = new ArrayList<>();
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(GET_EVENTS, id);
        while (sqlRowSet.next()) {
            list.add(Event.builder()
                    .eventId(sqlRowSet.getLong("event_id"))
                    .userId(sqlRowSet.getLong("user_id"))
                    .eventType(sqlRowSet.getString("event_type"))
                    .operation(sqlRowSet.getString("event_operation"))
                    .timestamp(sqlRowSet.getLong("time_stamp"))
                    .entityId(sqlRowSet.getLong("entity_id")).build());

            Comparator<Event> mapComparator = Comparator.comparing(Event::getTimestamp).reversed();
            list.sort(mapComparator);
        }
        return list;
    }

    /**
     * Сохраняет событие в таблицу events.
     *
     * @param object    Сущьность с которой произошло событие
     * @param eventType Тип события
     * @param operation Подтип события
     */
    @Override
    public void add(Object object, EventType eventType, EventOperations operation) {
        if (object instanceof Like) {
            jdbcTemplate.update("INSERT INTO events (USER_ID, EVENT_TYPE, EVENT_OPERATION, TIME_STAMP, ENTITY_ID) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    ((Like) object).getUser().getId(),
                    eventType.toString(),
                    operation.toString(),
                    Instant.now().toEpochMilli(),
                    ((Like) object).getFilm().getId());
        } else if (object instanceof Friendship) {
            jdbcTemplate.update("INSERT INTO events (USER_ID, EVENT_TYPE, EVENT_OPERATION, TIME_STAMP, ENTITY_ID) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    ((Friendship) object).getUser().getId(),
                    eventType.toString(),
                    operation.toString(),
                    Instant.now().toEpochMilli(),
                    ((Friendship) object).getFriend().getId());
        } else if (object instanceof Review) {
            jdbcTemplate.update("INSERT INTO events (USER_ID, EVENT_TYPE, EVENT_OPERATION, TIME_STAMP, ENTITY_ID) " +
                            "VALUES (?, ?, ?, ?, ?)",
                    ((Review) object).getUserId(),
                    eventType.toString(),
                    operation.toString(),
                    Instant.now().toEpochMilli(),
                    ((Review) object).getFilmId());
        }
    }
    private Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {
        if (rs.getRow() != 0) {
            Event event = new Event(

            );
            Film film = new Film(rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getInt("duration"),
                    mpaService.findMpaById(rs.getInt("FILM_RATING_ID"))
            );
            film.setId(rs.getInt("film_id"));
            addLikeForFilm(film);
            addGenreForFilm(film);
            return film;
        } else {
            throw new FilmNotFoundException("Нет MPA в базе");
        }
    }
}

