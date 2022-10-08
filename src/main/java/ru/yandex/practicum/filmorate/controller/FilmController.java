package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    public FilmController(FilmService filmService) {
        this.service = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Film editFilm(@Valid @RequestBody Film film) {
        return service.editFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        return service.getFilms();
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable String filmId, @PathVariable String userId) {
        service.addLike(Long.parseLong(filmId), Long.parseLong(userId));
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable String filmId, @PathVariable String userId) {
        service.removeLike(Long.parseLong(filmId), Long.parseLong(userId));
    }

    @GetMapping("/{filmId}")
    public Film getById(@PathVariable String filmId) {
        return service.getFilmById(Long.parseLong(filmId));
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(required = false) String count) {
        return service.getMostPopularFilms(count == null || count.isEmpty() ? 10 : Integer.parseInt(count));
    }
}
