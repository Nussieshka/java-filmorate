package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.util.Validation.validateFilmDate;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films;

    private long maxId = 0L;

    public InMemoryFilmStorage() {
        this.films = new HashMap<>();
    }

    @Override
    public Film addFilm(Film film) {
        validateFilmDate(film);
        if (film.getId() != null) {
            throw new RuntimeException("Cannot post film with id");
        }
        film.setId(++maxId);
        films.put(film.getId(), film);
        log.info("New film added successfully");
        return film;
    }

    @Override
    public Film editFilm(Film film) {
        validateFilmDate(film);
        Long currentId = film.getId();
        if (getById(currentId) == null) {
            throw new ObjectNotFoundException();
        }
        films.put(currentId, film);
        log.info("Film updated successfully");
        return film;
    }

    @Override
    public Film getById(long id) {
        return films.get(id);
    }

    @Override
    public List<Film> getFilms()  {
        return new ArrayList<>(films.values());
    }
}
