package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.util.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DbGenreStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genre";

        return this.jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    @Override
    public Optional<Genre> getGenreById(Integer id) {
        try {
            return Optional.ofNullable(this.jdbcTemplate.queryForObject("SELECT * FROM genre WHERE id = ? LIMIT 1",
                    this::makeGenre,id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Genre makeGenre(ResultSet resultSet, Integer i) throws SQLException {
        return Genre.valueToGenre(resultSet.getInt("id"));
    }

}
