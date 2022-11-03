package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService service;

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.ok(service.addFilm(film));
    }

    @PutMapping
    public ResponseEntity<Film> editFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.ok(service.editFilm(film));
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
    public ResponseEntity<Film> getById(@PathVariable String filmId) {
        return ResponseEntity.ok(service.getFilmById(Long.parseLong(filmId)));
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(required = false) String count) {
        return service.getMostPopularFilms(count);
    }
}
