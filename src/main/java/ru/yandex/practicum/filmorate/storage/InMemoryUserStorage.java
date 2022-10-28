package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users;

    private long maxId = 0;

    public InMemoryUserStorage() {
        this.users = new HashMap<>();
    }

    @Override
    public User addUser(User user) {
        if (user.getId() != null) {
            throw new RuntimeException("Cannot post user with id");
        }
        String username = user.getName();
        if (username == null || username.isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(++maxId);
        users.put(user.getId(), user);
        log.info("New user added successfully");
        return user;
    }

    @Override
    public User editUser(User user) {
        Long currentId = user.getId();
        User tempUser = getById(currentId);
        if (tempUser == null) {
            throw new ObjectNotFoundException();
        }
        users.put(currentId, user);
        log.info("User updated successfully");
        return user;
    }

    @Override
    public User getById(long id) {
        return users.get(id);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
