package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.util.MPA;

import java.util.List;
import java.util.Optional;

public interface MpaStorage {

    List<Optional<MPA>> getAllMpa();

    Optional<MPA> getMpaById(Integer id);
}
