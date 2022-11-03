package ru.yandex.practicum.filmorate.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = EnumSerializer.class)
public enum MPA {
    G,
    PG,
    PG_13,
    R,
    NC_17;

    @JsonCreator
    public static MPA valueToMpa(@JsonProperty("id") int id) {
        return values()[id - 1];
    }

    @Override
    public String toString() {
        return this.name().replaceAll("_", "-");
    }
}