package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.FriendStatus;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component("dbUserStorage")
public class DbUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<User> addUser(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sqlQuery = "INSERT INTO users(email, login, name, birthday) VALUES(?, ?, ?, ?)";

        int affectedRowsCount = this.jdbcTemplate.update(c -> userCreateStatement(c, user, sqlQuery), keyHolder);

        if (affectedRowsCount == 0) {
            throw new RuntimeException("No rows were affected after executing SQL Query");
        }

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        user.setId(id);
        return Optional.of(user);
    }

    @Override
    public Optional<User> editUser(User user) {
        String sqlQuery = "MERGE INTO users(id, email, login, name, birthday) KEY(id) VALUES(?, ?, ?, ?, ?)";
        long currentUserId = user.getId();

        if (getById(currentUserId).isEmpty())
            throw new ObjectNotFoundException();

        int affectedRowsCount = this.jdbcTemplate.update(sqlQuery,
                currentUserId,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());

        if (affectedRowsCount == 0) {
            throw new RuntimeException("No rows were affected after executing SQL Query");
        }

        return Optional.of(user);
    }

    @Override
    public Optional<User> getById(long id) {
        try {
            return Optional.ofNullable(this.jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ? LIMIT 1",
                    this::makeUser, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getUsers() {
        return this.jdbcTemplate.query("SELECT * FROM users", this::makeUser);
    }

    private User makeUser(ResultSet resultSet, int i) throws SQLException {
        User user;
        try {
            user = User.builder()
                    .id(resultSet.getLong("id"))
                    .email(resultSet.getString("email"))
                    .login(resultSet.getString("login"))
                    .name(resultSet.getString("name"))
                    .birthday(resultSet.getDate("birthday").toLocalDate())
                    .build();
            setFriends(user);
        } catch (NullPointerException e) {
            return null;
        }
        return user;
    }

    private void setFriends(User user) {
        for (Map.Entry<Long, FriendStatus> statusEntry : getFriendStatusMap(user.getId()).entrySet()) {
            user.addFriend(statusEntry.getKey(), statusEntry.getValue());
        }
    }

    private Map<Long, FriendStatus> getFriendStatusMap(Long id) {
        String sqlQuery = "SELECT * FROM friends WHERE first_user_id = ?";
        return this.jdbcTemplate.query(sqlQuery, resultSet -> {
            Map<Long, FriendStatus> friends = new HashMap<>();
            while (resultSet.next()) {
                friends.put(resultSet.getLong("second_user_id"),
                        FriendStatus.valueOf(resultSet.getString("status")));
            }
            return friends;
        }, id);
    }


    public void addFriendsToDb(Long userId1, Long userId2, FriendStatus friendStatus) {
        String sqlQuery = "MERGE INTO friends KEY(first_user_id, second_user_id) VALUES(?, ?, ?)";
        this.jdbcTemplate.update(sqlQuery, userId1, userId2, friendStatus.name());
    }

    public void removeFriendsFromDb(Long userId1, Long userId2) {

        String sqlQuery = "DELETE FROM friends WHERE first_user_id = ? AND second_user_id = ?";

        this.jdbcTemplate.update(sqlQuery, userId1, userId2);
    }

    private PreparedStatement userCreateStatement(Connection connection, User user, String sqlQuery) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[] {"id"});
        statement.setString(1, user.getEmail());
        String login = user.getLogin();
        statement.setString(2, login);
        String name = user.getName();
        String setName = name == null || name.isEmpty() ? login : name;
        statement.setString(3, setName);
        user.setName(setName);
        statement.setDate(4, Date.valueOf(user.getBirthday()));
        return statement;
    }
}
