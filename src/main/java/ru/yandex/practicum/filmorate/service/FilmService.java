package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.db.DbFilmStorage;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService {

    public final FilmStorage storage;

    public FilmService(@Qualifier("dbFilmStorage") FilmStorage storage) {
        this.storage = storage;
    }

    public Film addFilm(Film film) {
        Optional<Film> optionalFilm = storage.addFilm(film);
        if (optionalFilm.isEmpty())
            throw new ObjectNotFoundException();
        return optionalFilm.get();
    }

    public Film editFilm(Film film) {
        Optional<Film> optionalFilm = storage.editFilm(film);
        if (optionalFilm.isEmpty())
            throw new ObjectNotFoundException();
        return optionalFilm.get();
    }

    public List<Film> getFilms() {
        return storage.getFilms();
    }

    public void addLike(long filmId, long userId) {
        Optional<Film> film = storage.getById(filmId);
        if (film.isEmpty()) {
            throw new ObjectNotFoundException();
        }
        film.get().like(userId);
        if (storage instanceof DbFilmStorage) {
            ((DbFilmStorage) storage).likeFilm(filmId, userId);
        }
    }

    public void removeLike(long filmId, long userId) {
        Optional<Film> optionalFilm = storage.getById(filmId);
        if (optionalFilm.isEmpty()) {
            throw new ObjectNotFoundException();
        }
        Film film = optionalFilm.get();
        if (!film.getLikedBy().contains(userId)) {
            throw new ObjectNotFoundException();
        }
        film.dislike(userId);
        if (storage instanceof DbFilmStorage) {
            ((DbFilmStorage) storage).dislikeFilm(filmId, userId);
        }
    }

    public Film getFilmById(long id) {
        Optional<Film> film = storage.getById(id);
        if (film.isEmpty()) {
            throw new ObjectNotFoundException();
        }
        return film.get();
    }

    public List<Film> getMostPopularFilms(String stringCount) {
        int count = stringCount == null || stringCount.isEmpty() ? 10 : Integer.parseInt(stringCount);
        return getFilms()
                .stream()
                .sorted(Comparator.comparingInt(o -> -1 * o.getLikesCount()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
