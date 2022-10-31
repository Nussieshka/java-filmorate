package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> addFilm(Film film);
    Optional<Film> editFilm(Film film);
    Optional<Film> getById(long id);
    List<Film> getFilms();
}
