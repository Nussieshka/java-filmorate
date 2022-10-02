package ru.yandex.practicum.filmorate.util;

public class ValidationException extends RuntimeException {

    public <T> ValidationException(String fieldName, T field) {
        super("Field \"" + fieldName + "\" with value \"" + field + "\" is invalid.");
    }
}
