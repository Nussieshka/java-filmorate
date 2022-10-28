package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.storage.db.DbUserStorage;
import ru.yandex.practicum.filmorate.util.FriendStatus;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage storage;

    public UserService(@Qualifier("dbUserStorage") UserStorage storage) {
        this.storage = storage;
    }

    public Optional<User> addUser(User user) {
        return storage.addUser(user);
    }

    public Optional<User> editUser(User user) {
        return storage.editUser(user);
    }

    public List<Optional<User>> getUsers() {
        return storage.getUsers();
    }

    public Optional<User> getUserById(long id) {
        Optional<User> user = storage.getById(id);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException();
        }
        return user;
    }

    public void addToFriendList(long id1, long id2) {
        User[] temp = getValidFriendsById(id1, id2);

        User user1 = temp[0];
        User user2 = temp[1];

        Map<Long, FriendStatus> user1Friends = user1.getFriends();
        Map<Long, FriendStatus> user2Friends = user2.getFriends();

        if (user2Friends.containsKey(id1) && user2Friends.get(id1).equals(FriendStatus.REQUEST)) {
            throw new IllegalArgumentException();
        }

        else if (user1Friends.containsKey(id2) && user1Friends.get(id2).equals(FriendStatus.REQUEST)) {
            user2.addFriend(id1, FriendStatus.FRIEND);
            if (storage instanceof DbUserStorage) {
                DbUserStorage dbUserStorage = (DbUserStorage) storage;
                dbUserStorage.addFriendsToDb(id2, id1, FriendStatus.FRIEND);
            }
            return;
        }

        user1.addFriend(id2, FriendStatus.FRIEND);
        user2.addFriend(id1, FriendStatus.REQUEST);

        if (storage instanceof DbUserStorage) {
            DbUserStorage dbUserStorage = (DbUserStorage) storage;
            dbUserStorage.addFriendsToDb(id1, id2, FriendStatus.FRIEND);
            dbUserStorage.addFriendsToDb(id2, id1, FriendStatus.REQUEST);
        }
    }

    public void removeFromFriendList(long id1, long id2) {
        User[] temp = getValidFriendsById(id1, id2);

        User user1 = temp[0];
        User user2 = temp[1];

        if (!user1.getFriends().containsKey(id2) && !user2.getFriends().containsKey(id1)) {
            throw new IllegalArgumentException();
        }

        user1.removeFriend(id2);
        user2.removeFriend(id1);

        if (storage instanceof DbUserStorage) {
            ((DbUserStorage) storage).removeFriendsFromDb(id1, id2);
        }
    }

    public User[] getValidFriendsById(long id1, long id2) {
        if (id1 == id2) {
            throw new IllegalArgumentException();
        }

        Optional<User> user1 = getUserById(id1);
        Optional<User> user2 = getUserById(id2);

        if (user1.isEmpty() || user2.isEmpty()) {
            throw new ObjectNotFoundException();
        }

        return new User[] { user1.get(), user2.get() };
    }

    public Set<User> getFriends(long id) {
        Optional<User> user = storage.getById(id);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException();
        }
        return getFriendsAsUser(user.get());
    }

    private Set<User> getFriendsAsUser(User user) {
        return user.getFriends().entrySet().stream()
                .filter(x -> x.getValue().equals(FriendStatus.FRIEND))
                .map(Map.Entry::getKey)
                .map(storage::getById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    public Set<User> getCommonFriends(long id, long otherId) {
        if (id == otherId) {
            throw new IllegalArgumentException();
        }
        Optional<User> user1 = storage.getById(id);
        Optional<User> user2 = storage.getById(otherId);
        if (user1.isEmpty() || user2.isEmpty()) {
            throw new ObjectNotFoundException();
        }
        Set<User> friends1 = getFriendsAsUser(user1.get());
        Set<User> friends2 = getFriendsAsUser(user2.get());
        return friends1.stream().filter(friends2::contains).collect(Collectors.toSet());
    }
}
