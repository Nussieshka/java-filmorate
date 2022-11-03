package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.db.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.db.DbGenreStorage;
import ru.yandex.practicum.filmorate.storage.db.DbMpaStorage;
import ru.yandex.practicum.filmorate.storage.db.DbUserStorage;
import ru.yandex.practicum.filmorate.util.Genre;
import ru.yandex.practicum.filmorate.util.MPA;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FilmorateApplicationTests {
	private final DbUserStorage userStorage;
	private final DbFilmStorage filmStorage;
	private final DbMpaStorage mpaStorage;
	private final DbGenreStorage dbGenreStorage;

	@Test
	@Order(1)
	public void shouldAddUserToDB() {
		User user = createTestUser();
		Optional<User> testOptionalUser = userStorage.addUser(user);
		assertTrue(testOptionalUser.isPresent());
		User testUser = testOptionalUser.get();
		assertEquals(1, testUser.getId());
		assertEquals(user.getName(), testUser.getName());
		assertEquals(user.getLogin(), testUser.getLogin());
		assertEquals(user.getEmail(), testUser.getEmail());
		assertEquals(user.getBirthday(), testUser.getBirthday());
	}

	@Test
	@Order(2)
	public void shouldGetValidUserFromDB() {
		User user = createTestUser();
		Optional<User> testOptionalUserAdd = userStorage.addUser(user);
		assertTrue(testOptionalUserAdd.isPresent());
		User testUserAdd = testOptionalUserAdd.get();
		Optional<User> testOptionalUserGet = userStorage.getById(testUserAdd.getId());
		assertTrue(testOptionalUserGet.isPresent());
		assertEquals(testUserAdd, testOptionalUserGet.get());
	}

	@Test
	@Order(3)
	public void shouldGetValidListOfUsers() {
		List<User> optionalList = userStorage.getUsers();
		assertEquals(optionalList.size(), 2);

		for (int i = 0; i < optionalList.size(); i++) {
			assertEquals(i + 1, optionalList.get(i).getId());
		}
	}

	@Test
	@Order(4)
	public void shouldCorrectlyEditUser() {
		User newUser = User.builder()
				.id(1L)
				.login("bbbbb")
				.name("aaaaa")
				.email("mmm@gmail.com")
				.birthday(LocalDate.of(1981, 3, 3))
				.build();
		Optional<User> testUserEdit = userStorage.editUser(newUser);
		assertTrue(testUserEdit.isPresent());
		assertEquals(newUser, testUserEdit.get());

		Optional<User> testUserGet = userStorage.getById(1);
		assertTrue(testUserGet.isPresent());
		assertEquals(testUserGet.get(), newUser);
	}

	private User createTestUser() {
		return User.builder()
				.login("dolore")
				.name("asdsad")
				.email("bbb@gmail.com")
				.birthday(LocalDate.of(1984, 1, 1))
				.build();
	}

	@Test
	@Order(5)
	public void shouldAddFilmToDB() {
		Film film = createTestFilm();
		Optional<Film> testOptionalFilm = filmStorage.addFilm(film);
		assertTrue(testOptionalFilm.isPresent());
		Film testFilm = testOptionalFilm.get();
		assertEquals(1, testFilm.getId());
		assertEquals(film.getName(), testFilm.getName());
		assertEquals(film.getReleaseDate(), testFilm.getReleaseDate());
		assertEquals(film.getDescription(), testFilm.getDescription());
		assertEquals(film.getDuration(), testFilm.getDuration());
		assertEquals(film.getMpa(), testFilm.getMpa());
		assertEquals(film.getGenres(), testFilm.getGenres());
	}

	@Test
	@Order(6)
	public void shouldGetValidFilmFromDB() {
		Film film = createTestFilm();
		Optional<Film> testOptionalFilm = filmStorage.addFilm(film);
		assertTrue(testOptionalFilm.isPresent());
		Film testFilmAdd = testOptionalFilm.get();
		Optional<Film> testOptionalFilmGet = filmStorage.getById(testFilmAdd.getId());
		assertTrue(testOptionalFilmGet.isPresent());
		assertEquals(testFilmAdd, testOptionalFilmGet.get());
	}

	@Test
	@Order(7)
	public void shouldGetValidListOfFilms() {
		List<Film> optionalList = filmStorage.getFilms();
		assertEquals(optionalList.size(), 2);

		for (int i = 0; i < optionalList.size(); i++) {
			assertEquals(i + 1, optionalList.get(i).getId());
		}
	}

	@Test
	@Order(8)
	public void shouldCorrectlyEditFilm() {
		Film newFilm = Film.builder()
				.id(1L)
				.name("asd asdasdasd")
				.releaseDate(LocalDate.of(1999, 1, 3))
				.description("Lkj asdasd awdsff")
				.duration(Duration.ofMinutes(111))
				.mpa(MPA.valueToMpa(3))
				.genres(EnumSet.of(Genre.valueToGenre(2), Genre.valueToGenre(4)))
				.build();
		Optional<Film> testFilmEdit = filmStorage.editFilm(newFilm);
		assertTrue(testFilmEdit.isPresent());
		assertEquals(newFilm, testFilmEdit.get());

		Optional<Film> testFilmGet = filmStorage.getById(1);
		assertTrue(testFilmGet.isPresent());
		assertEquals(testFilmGet.get(), newFilm);
	}

	private Film createTestFilm() {
		return Film.builder()
				.name("labore nulla")
				.releaseDate(LocalDate.of(1979, 4, 17))
				.description("Duis in consequat esse")
				.duration(Duration.ofMinutes(100))
				.mpa(MPA.valueToMpa(1))
				.genres(EnumSet.of(Genre.valueToGenre(1), Genre.valueToGenre(3)))
				.build();
	}

	@Test
	@Order(9)
	public void shouldGetValidListOfMpas() {
		List<MPA> mpaList = Arrays.asList(MPA.values());
		List<MPA> mpaGetList = mpaStorage.getAllMpa();
		assertEquals(mpaList, mpaGetList);
	}

	@Test
	@Order(10)
	public void shouldGetValidMpa() {
		MPA[] mpas = MPA.values();
		for (int i = 0; i < mpas.length; i++) {
			assertEquals(mpas[i], mpaStorage.getMpaById(i + 1).orElse(null));
		}
	}

	@Test
	@Order(11)
	public void shouldGetValidListOfGenres() {
		List<Genre> genresList = Arrays.asList(Genre.values());
		List<Genre> genresGetList = dbGenreStorage.getAllGenres();
		assertEquals(genresList, genresGetList);
	}

	@Test
	@Order(12)
	public void shouldGetValidGenres() {
		Genre[] genres = Genre.values();
		for (int i = 0; i < genres.length; i++) {
			assertEquals(genres[i], dbGenreStorage.getGenreById(i + 1).orElse(null));
		}
	}
}