package ru.yandex.practicum.filmorate.model;

public enum EventOperations {
    REMOVE("REMOVE"),
    ADD("ADD"),
    UPDATE("UPDATE");
    private final String operation;

    EventOperations(String operation) {
        this.operation = operation;
    }

    public String getOperation() {
        return operation;
    }
}
