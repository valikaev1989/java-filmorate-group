package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.storage.EventsStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class EventDbStorage implements EventsStorage {
    private static final String GET_EVENTS = "SELECT E.EVENT_ID, E.TIME_STAMP, E.USER_ID, E.EVENT_TYPE, " +
            "E.EVENT_OPERATION, E.ENTITY_ID " +
            "FROM EVENTS E " +
            "JOIN USERR U ON E.USER_ID = U.USER_ID " +
            "JOIN FRIENDS F ON U.USER_ID = F.FRIEND_ID " +
            "WHERE F.USER_ID  = ? ORDER BY E.TIME_STAMP DESC";
    private final JdbcTemplate jdbcTemplate;

    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> getEvents(Long id) {
        return jdbcTemplate.query(GET_EVENTS, this::mapRowToEvent, id);
    }

    @Override
    public void addEvent(Long userId, Long entityId, EventType eventType, EventOperations operation) {
        Event event = new Event(userId, entityId, eventType.getType(), operation.getOperation());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("events")
                .usingGeneratedKeyColumns("event_id");
        insert.executeAndReturnKey(event.toMapEvent()).longValue();
    }

    private Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {
        if (rs.getRow() != 0) {
            return new Event(
                    rs.getLong("event_id"),
                    rs.getLong("user_id"),
                    rs.getLong("entity_id"),
                    rs.getString("event_type"),
                    rs.getString("event_operation"),
                    rs.getLong("time_stamp")
            );
        } else {
            throw new ModelNotFoundException("событие не найдено");
        }
    }
}