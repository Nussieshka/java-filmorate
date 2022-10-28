package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.util.MPA;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {
    private final MpaService mpaService;

    @GetMapping
    public List<Optional<MPA>> getGenres() {
        return mpaService.getMpa();
    }

    @GetMapping("/{id}")
    public Optional<MPA> getMpaById(@PathVariable String id) {
        return mpaService.getMpaById(id);
    }
}
