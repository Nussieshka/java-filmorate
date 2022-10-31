package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.util.MPA;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class MpaService {
    private final MpaStorage storage;

    public MpaService(MpaStorage storage) {
        this.storage = storage;
    }

    public List<MPA> getMpa() {
        return storage.getAllMpa();
    }

    public MPA getMpaById(String id) {
        Optional<MPA> mpa = storage.getMpaById(Integer.parseInt(id));
        if (mpa.isEmpty()) {
            throw new ObjectNotFoundException();
        }
        return mpa.get();
    }
}
