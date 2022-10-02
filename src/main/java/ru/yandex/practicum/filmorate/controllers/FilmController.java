package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.ValidationException;

import javax.validation.Valid;

import static ru.yandex.practicum.filmorate.util.Validation.validateFilmDate;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final List<Film> films;

    public FilmController() {
        this.films = new ArrayList<>();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        validateFilmDate(film);
        films.add(film);
        log.info("New film added successfully");
        return film;
    }

    @PutMapping
    public Film editFilm(@Valid @RequestBody Film film) {
        validateFilmDate(film);
        films.stream().filter(x -> x.getId() == film.getId()).findFirst().ifPresent(films::remove);
        films.add(film);
        log.info("Film updated successfully");
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return films;
    }

    @ExceptionHandler(value = { ValidationException.class, MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleValidationException(Exception exception) {
        log.error((exception instanceof ValidationException ? "ValidationException " : "MethodArgumentNotValidException ")
                + "was thrown");
        log.error(exception.getMessage());
    }
}
