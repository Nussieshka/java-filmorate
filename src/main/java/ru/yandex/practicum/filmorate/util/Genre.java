package ru.yandex.practicum.filmorate.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = EnumSerializer.class)
public enum Genre {
    COMEDY("Комедия"),
    DRAMA("Драма"),
    CARTOON("Мультфильм"),
    THRILLER("Триллер"),
    DOCUMENTARY("Документальный"),
    ACTION("Боевик");

    private final String name;
    Genre(String name) {
        this.name = name;
    }

    @JsonCreator
    public static Genre valueToGenre(@JsonProperty("id") int id) {
        return values()[id - 1];
    }

    @Override
    public String toString() {
        return this.name;
    }
}
