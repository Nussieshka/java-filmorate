package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.util.Validation.validateFilmDate;

@Component("inMemoryFilmStorage")
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films;

    private long maxId = 0L;

    public InMemoryFilmStorage() {
        this.films = new HashMap<>();
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        validateFilmDate(film);
        if (film.getId() != null) {
            throw new RuntimeException("Cannot post film with id");
        }
        film.setId(++maxId);
        films.put(film.getId(), film);
        log.info("New film added successfully");
        return Optional.of(film);
    }

    @Override
    public Optional<Film> editFilm(Film film) {
        validateFilmDate(film);
        Long currentId = film.getId();
        if (getById(currentId).isEmpty()) {
            throw new ObjectNotFoundException();
        }
        films.put(currentId, film);
        log.info("Film updated successfully");
        return Optional.of(film);
    }

    @Override
    public Optional<Film> getById(long id) {
        return Optional.of(films.get(id));
    }

    @Override
    public List<Optional<Film>> getFilms()  {
        return films.values().stream().map(Optional::of).collect(Collectors.toList());
    }
}
