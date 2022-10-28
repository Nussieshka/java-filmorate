package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public Optional<User> addUser(@Valid @RequestBody User user) {
        return service.addUser(user);
    }

    @PutMapping
    public Optional<User> editUser(@Valid @RequestBody User user) {
        return service.editUser(user);
    }

    @GetMapping
    public List<Optional<User>> getUsers() {
        return service.getUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriendList(@PathVariable String id, @PathVariable String friendId) {
        service.addToFriendList(Long.parseLong(id), Long.parseLong(friendId));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriendList(@PathVariable String id, @PathVariable String friendId) {
        service.removeFromFriendList(Long.parseLong(id), Long.parseLong(friendId));
    }

    @GetMapping("/{id}/friends")
    public Set<User> getFriends(@PathVariable String id) {
        return service.getFriends(Long.parseLong(id));
    }

    @GetMapping("/{userId}")
    public Optional<User> getById(@PathVariable String userId) {
        return service.getUserById(Long.parseLong(userId));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Set<User> getCommonFriends(@PathVariable String id, @PathVariable String otherId) {
        return service.getCommonFriends(Long.parseLong(id), Long.parseLong(otherId));
    }
}
