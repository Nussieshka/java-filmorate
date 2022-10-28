package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.util.Genre;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<Optional<Genre>> getGenres() {
        return genreService.getGenres();
    }

    @GetMapping("/{id}")
    public Optional<Genre> getGenreById(@PathVariable String id) {
        return genreService.getGenreById(id);
    }
}
