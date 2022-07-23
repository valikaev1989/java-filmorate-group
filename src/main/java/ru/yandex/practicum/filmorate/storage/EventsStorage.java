package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventType;

import java.util.List;

public interface EventsStorage {

    List<Event> getEvents(Long id);

    void add(Object object, EventType eventType, EventOperations operation);
}
