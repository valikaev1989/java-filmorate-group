package ru.yandex.practicum.filmorate.model;

public class CreatorId {
    private static int userId = 1;
    private static int filmId = 1;


    public static int createUserId() {
        return userId++;
    }

    public static int createFilmId() {
        return filmId++;
    }

    public static void setUserId() {
        userId = 1;
    }

    public static void setFilmId() {
        filmId = 1;
    }
}
