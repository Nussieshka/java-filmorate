package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.util.Genre;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    private final GenreStorage storage;

    public GenreService(GenreStorage storage) {
        this.storage = storage;
    }

    public List<Genre> getGenres() {
        return storage.getAllGenres();
    }

    public Genre getGenreById(String id) {
        Optional<Genre> genre = storage.getGenreById(Integer.parseInt(id));
        if (genre.isEmpty()) {
            throw new ObjectNotFoundException();
        }
        return genre.get();
    }
}
