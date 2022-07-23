package ru.yandex.practicum.filmorate.model;

public enum EventType {
    LIKE("LIKE"),
    FRIEND("FRIEND"),
    REVIEW("REVIEW"),
    ;
    private final String type;

    EventType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
