package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    public final FilmStorage storage;

    public FilmService(FilmStorage storage) {
        this.storage = storage;
    }

    public Film addFilm(Film film) {
        return storage.addFilm(film);
    }

    public Film editFilm(Film film) {
        return storage.editFilm(film);
    }

    public List<Film> getFilms() {
        return storage.getFilms();
    }

    public void addLike(long filmId, long userId) {
        Film film = storage.getById(filmId);
        if (film == null) {
            throw new ObjectNotFoundException();
        }
        film.like(userId);
    }

    public void removeLike(long filmId, long userId) {
        Film film = storage.getById(filmId);
        if (film == null) {
            throw new ObjectNotFoundException();
        }
        if (!film.getLikedBy().contains(userId)) {
            throw new ObjectNotFoundException();
        }
        film.dislike(userId);
    }

    public Film getFilmById(long id) {
        Film film = storage.getById(id);
        if (film == null) {
            throw new ObjectNotFoundException();
        }
        return film;
    }

    public List<Film> getMostPopularFilms(int count) {
        return getFilms()
                .stream()
                .sorted(Comparator.comparingInt(o -> -1 * o.getLikesCount()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
