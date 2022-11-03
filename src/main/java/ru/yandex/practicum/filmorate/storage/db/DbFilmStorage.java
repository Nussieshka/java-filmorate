package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.Genre;
import ru.yandex.practicum.filmorate.util.MPA;
import ru.yandex.practicum.filmorate.util.ObjectNotFoundException;

import java.sql.*;
import java.sql.Date;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.util.Validation.validateFilmDate;

@Component("dbFilmStorage")
public class DbFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        validateFilmDate(film);
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sqlQuery = "INSERT INTO movies(name, description, release_date, duration, rating_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        int affectedRowsCount = this.jdbcTemplate.update(c -> filmCreateStatement(c, film, sqlQuery), keyHolder);

        if (affectedRowsCount == 0) {
            throw new RuntimeException("No rows were affected after executing SQL Query");
        }

        Long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        film.setId(id);
        setGenresForFilm(film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> editFilm(Film film) {
        validateFilmDate(film);
        String sqlQuery = "MERGE INTO movies(id, name, description, release_date, duration, rating_id) KEY(id) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        long currentFilmId = film.getId();

        if (getById(currentFilmId).isEmpty())
            throw new ObjectNotFoundException();

        int affectedRowsCount = this.jdbcTemplate.update(sqlQuery,
                currentFilmId,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().ordinal() + 1);

        if (affectedRowsCount == 0) {
            throw new RuntimeException("No rows were affected after executing SQL Query");
        }

        this.jdbcTemplate.update("DELETE FROM movie_genres WHERE movie_id = ?", currentFilmId);
        setGenresForFilm(film);
        return Optional.of(film);
    }

    @Override
    public Optional<Film> getById(long id) {
        try {
            return Optional.ofNullable(this.jdbcTemplate.queryForObject("SELECT * FROM movies WHERE id = ? LIMIT 1",
                    this::makeFilm, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getFilms() {
        return this.jdbcTemplate.query("SELECT * FROM movies", this::makeFilm);
    }

    private Film makeFilm(ResultSet resultSet, int i) throws SQLException {
        Film film;
        try {
            film = Film.builder()
                    .id(resultSet.getLong("id"))
                    .name(resultSet.getString("name"))
                    .description(resultSet.getString("description"))
                    .releaseDate(resultSet.getDate("release_date").toLocalDate())
                    .duration(Duration.ofSeconds(resultSet.getLong("duration")))
                    .mpa(MPA.valueToMpa(resultSet.getInt("rating_id")))
                    .build();
            film.setGenres(getGenres(film.getId()).stream().collect(Collectors.toCollection(()
                    -> EnumSet.noneOf(Genre.class))));
            setLikes(film);
        } catch (NullPointerException e) {
            return null;
        }
        return film;
    }

    private PreparedStatement filmCreateStatement(Connection connection, Film film, String sqlQuery) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sqlQuery, new String[] {"id"});
        statement.setString(1, film.getName());
        statement.setString(2, film.getDescription());
        statement.setDate(3, Date.valueOf(film.getReleaseDate()));
        statement.setLong(4, film.getDuration());
        statement.setInt(5, film.getMpa().ordinal() + 1);
        return statement;
    }

    private Set<Genre> getGenres(long id) {
        return new HashSet<>(
                this.jdbcTemplate.query("SELECT * FROM movie_genres WHERE movie_id = ? ORDER BY genre_id DESC",
                        (resultSet, i) -> Genre.valueToGenre(resultSet.getInt("genre_id")), id));
    }

    private void setGenresForFilm(Film film) {
        for (Genre genreId : film.getGenres()) {
            if (this.jdbcTemplate.update("INSERT INTO movie_genres VALUES (?, ?)",
                    film.getId(), genreId.ordinal() + 1) == 0) {
                throw new RuntimeException("No rows were affected after executing SQL Query");
            }
        }
    }

    public void likeFilm(long filmId, long userId) {
        String sqlQuery = "INSERT INTO movie_likes VALUES(?, ?)";
        this.jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void dislikeFilm(long filmId, long userId) {
        String sqlQuery = "DELETE FROM movie_likes WHERE movie_id = ? AND user_id = ?";
        this.jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    private void setLikes(Film film) {
        String sqlQuery = "SELECT user_id FROM movie_likes WHERE movie_id = ?";
        List<Long> usersIds = this.jdbcTemplate.query(sqlQuery, (rs, i) -> rs.getLong("user_id"), film.getId());
        for (Long id : usersIds)
            film.like(id);
    }
}