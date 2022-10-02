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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films;

    private int maxId = 0;

    public FilmController() {
        this.films = new HashMap<>();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        validateFilmDate(film);
        if (film.getId() != null) throw new RuntimeException("Cannot post film with id");
        film.setId(++maxId);
        films.put(film.getId(), film);
        log.info("New film added successfully");
        return film;
    }

    @PutMapping
    public Film editFilm(@Valid @RequestBody Film film) {
        validateFilmDate(film);
        Integer currentId = film.getId();
        Film tempFilm = films.get(currentId);
        if (tempFilm == null) throw new RuntimeException("There is no such film");
        films.put(currentId, film);
        log.info("Film updated successfully");
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleRuntimeException(Exception exception) {
        log.error(exception.getMessage());
    }

    @ExceptionHandler(value = { ValidationException.class, MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleValidationException(Exception exception) {
        log.error((exception instanceof ValidationException ? "ValidationException " : "MethodArgumentNotValidException ")
                + "was thrown");
        log.error(exception.getMessage());
    }
}
