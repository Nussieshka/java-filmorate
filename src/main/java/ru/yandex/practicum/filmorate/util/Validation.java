package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class Validation {

    public static void validateUser(@NotNull User user) throws ValidationException {
        String email = user.getEmail();
        String login = user.getLogin();
        LocalDate date = user.getBirthday();
        if (!email.contains("@")) throw new ValidationException("email", email);
        else if (login.isBlank() || login.isEmpty()) throw new ValidationException("login", login);
        else if (date.isAfter(LocalDate.now())) throw new ValidationException("date", date);
    }

    public static void validateFilm(@NotNull Film film) throws ValidationException {
        String name = film.getName();
        String description = film.getDescription();
        LocalDate date = film.getReleaseDate();
        long duration = film.getDuration();
        if (name.isEmpty()) throw new ValidationException("name", name);
        else if (description.length() > 200) throw new ValidationException("description", description);
        else if (date.isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("date", date);
        else if (duration < 0) throw new ValidationException("duration", duration);
    }

    public static void validateFilmDate(Film film) {
        LocalDate date = film.getReleaseDate();
        long duration = film.getDuration();
        if (date.isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("date", date);
        else if (duration < 0) throw new ValidationException("duration", duration);
    }
}
