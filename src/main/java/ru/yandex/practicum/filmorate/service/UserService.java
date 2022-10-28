package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage storage;

    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public User addUser(User user) {
        return storage.addUser(user);
    }

    public User editUser(User user) {
        return storage.editUser(user);
    }

    public List<User> getUsers() {
        return storage.getUsers();
    }

    public User getUserById(long id) {
        User user = storage.getById(id);
        if (user == null) {
            throw new ObjectNotFoundException();
        }
        return user;
    }

    public void addToFriendList(long id1, long id2) {
        if (id1 == id2) {
            throw new IllegalArgumentException();
        }

        User user1 = getUserById(id1);
        User user2 = getUserById(id2);

        if (user1 == null || user2 == null) {
            throw new ObjectNotFoundException();
        }

        else if (user1.getFriends().contains(id2) || user2.getFriends().contains(id1)) {
            throw new IllegalArgumentException();
        }

        user1.addFriend(id2);
        user2.addFriend(id1);
    }

    public void removeFromFriendList(long id1, long id2) {
        if (id1 == id2) {
            throw new IllegalArgumentException();
        }

        User user1 = getUserById(id1);
        User user2 = getUserById(id2);

        if (user1 == null || user2 == null) {
            throw new ObjectNotFoundException();
        }

        else if (!user1.getFriends().contains(id2) && !user2.getFriends().contains(id1)) {
            throw new IllegalArgumentException();
        }

        user1.removeFriend(id2);
        user2.removeFriend(id1);
    }

    public Set<User> getFriends(long id) {
        User user = storage.getById(id);
        if (user == null) {
            throw new ObjectNotFoundException();
        }
        return getFriendsAsUser(user);
    }

    private Set<User> getFriendsAsUser(User user) {
        return user.getFriends().stream().map(storage::getById).collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(long id, long otherId) {
        if (id == otherId) {
            throw new IllegalArgumentException();
        }
        User user1 = storage.getById(id);
        User user2 = storage.getById(otherId);
        if (user1 == null || user2 == null) {
            throw new ObjectNotFoundException();
        }
        Set<User> friends1 = getFriendsAsUser(user1);
        Set<User> friends2 = getFriendsAsUser(user2);
        return friends1.stream().filter(friends2::contains).collect(Collectors.toSet());
    }
}
