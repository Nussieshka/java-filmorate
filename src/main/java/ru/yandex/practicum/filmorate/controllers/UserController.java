package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.ValidationException;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final List<User> users;

    public UserController() {
        this.users = new ArrayList<>();
    }

    @PostMapping("/add")
    public User addUser(@Valid @RequestBody User user) {
        users.add(user);
        log.info("New user added successfully");
        return user;
    }

    @PutMapping("/edit")
    public User editUser(@Valid @RequestBody User user) {
        users.stream().filter(x -> x.getId() == user.getId()).findFirst().ifPresent(users::remove);
        users.add(user);
        log.info("User updated successfully");
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return users;
    }

    @ExceptionHandler(value = { ValidationException.class, MethodArgumentNotValidException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleValidationException(Exception exception) {
        log.error((exception instanceof ValidationException ? "ValidationException " : "MethodArgumentNotValidException ")
                + "was thrown");
        log.error(exception.getMessage());
    }
}
