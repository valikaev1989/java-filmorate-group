package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventsStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventsService {
    private final EventsStorage eventsStorage;
    private final UserService userService;

    public List<Event> getEvents(Long id) {
        userService.getUserById(id);
        return eventsStorage.getEvents(id);
    }
    public Event getEventById(Long eventId){
        return eventsStorage.getEventById(eventId);
    }
}
