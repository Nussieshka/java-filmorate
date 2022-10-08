package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ValidationException;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users;

    private int maxId = 0;

    public UserController() {
        this.users = new HashMap<>();
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        if (user.getId() != null) {
            throw new RuntimeException("Cannot post user with id");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(++maxId);
        users.put(user.getId(), user);
        log.info("New user added successfully");
        return user;
    }

    @PutMapping
    public User editUser(@Valid @RequestBody User user) {
        Integer currentId = user.getId();
        User tempUser = users.get(currentId);
        if (tempUser == null) {
            throw new RuntimeException("There is no such user");
        }
        users.put(currentId, user);
        log.info("User updated successfully");
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleRuntimeException(Exception exception) {
        log.error(exception.getMessage());
    }

    @ExceptionHandler(value = { ValidationException.class, MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleValidationException(Exception exception) {
        log.error((exception instanceof ValidationException ? "ValidationException " : "MethodArgumentNotValidException ")
                + "was thrown");
        log.error(exception.getMessage());
    }
}
