package ru.yandex.practicum.filmorate.storage.impl;

import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperations;
import ru.yandex.practicum.filmorate.model.EventType;
import ru.yandex.practicum.filmorate.storage.EventsStorage;

import java.util.List;

public class DatabaseEventsStorage implements EventsStorage {
    @Override
    public List<Event> getEvents(Long id) {
        return null;
    }

    @Override
    public void add(Object object, EventType eventType, EventOperations operation) {

    }
}
