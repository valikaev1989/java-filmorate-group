package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
public class EventDbStorage implements EventsStorage {
    private static final String GET_EVENTS = "SELECT * FROM events WHERE user_id = ?";
    private static final String GET_EVENT_BY_ID = "SELECT * FROM events WHERE event_id = ?";
    private final JdbcTemplate jdbcTemplate;

    public EventDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Event> getEvents(Long userId) {
        log.info("Start EventDbStorage.getEvents userId:{}.", userId);
        List<Event> events = jdbcTemplate.query(GET_EVENTS, this::mapRowToEvent, userId);
        log.info("End EventDbStorage.getEvents userId:{}.List<Event> events.size = {}", userId, events.size());
        return events;
    }

    @Override
    public void addEvent(Long userId, Long entityId, EventType eventType, EventOperations operation) {
        log.info("Start EventDbStorage.addEvent userId:{},entityId:{},eventType:{},operation:{}",
                userId, entityId,
                eventType, operation);
        Event event = new Event(userId, entityId, eventType.getType(), operation.getOperation());
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("events")
                .usingGeneratedKeyColumns("event_id");
        long eventId = insert.executeAndReturnKey(event.toMapEvent()).longValue();
        Event event1 = getEventById(eventId);
        log.info("End EventDbStorage.addEvent event добавлен:{}.", event1);
    }

    @Override
    public Event getEventById(Long eventId) {
        return jdbcTemplate.queryForObject(GET_EVENT_BY_ID, this::mapRowToEvent, eventId);
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