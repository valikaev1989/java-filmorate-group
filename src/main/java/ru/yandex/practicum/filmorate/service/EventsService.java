package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventsStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class EventsService {
    private final EventsStorage eventsStorage;
    private final UserService userService;

    public Collection<Event> getEvents(Long id) {
        userService.getUser(id);
        return eventsStorage.getEvents(id);
    }
}
