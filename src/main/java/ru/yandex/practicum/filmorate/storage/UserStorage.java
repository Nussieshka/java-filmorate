package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> addUser(User user);
    Optional<User> editUser(User user);
    Optional<User> getById(long id);
    List<User> getUsers();
}
