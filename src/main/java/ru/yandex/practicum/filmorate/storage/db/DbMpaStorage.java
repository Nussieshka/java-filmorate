package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.util.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DbMpaStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MPA> getAllMpa() {
        String sqlQuery = "SELECT * FROM rating";

        return this.jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    @Override
    public Optional<MPA> getMpaById(Integer id) {
        try {
            return Optional.ofNullable(this.jdbcTemplate.queryForObject("SELECT * FROM rating WHERE id = ? LIMIT 1",
                    this::makeMpa, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private MPA makeMpa(ResultSet resultSet, Integer i) throws SQLException {
        return MPA.valueToMpa(resultSet.getInt("id"));
    }

}
