package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmorateApplicationTests {

	@LocalServerPort
	private int port;
	private static String url;

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	void setUp() {
		url = "http://localhost:" + port + "/";
	}

	@Test
	void shouldPassUserValidation() {
		User user = new User(0, "email@gmail.com", "login", "name", LocalDate.now().minusDays(15));
		assertEquals(user, restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldPassFilmValidation() {
		Film film = new Film(0, "film", "desc", LocalDate.of(1912, 12, 28),
				Duration.ofHours(2));
		assertEquals(film, restTemplate.postForObject(url + "/films", film, Film.class));
	}

	@Test
	void shouldNotPassUserValidationWithEmptyLogin() {
		User user = new User(0, "email@gmail.com", "", "name", LocalDate.now().minusDays(15));
		assertNull(restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldNotPassUserValidationWithWrongEmail() {
		User user = new User(0, "email", "login", "name", LocalDate.now().minusDays(15));
		assertNull(restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldNotPassUserValidationWithEmptyEmail() {
		User user = new User(0, "", "login", "name", LocalDate.now().minusDays(15));
		assertNull(restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldPassUserValidationWithEmptyName() {
		User user = new User(0, "email@gmail.com", "login", "", LocalDate.now().minusDays(15));
		assertEquals(user, restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldNotPassUserValidationWithBlankLogin() {
		User user = new User(0, "email@gmail.com", " ", "name", LocalDate.now().minusDays(15));
		assertNull(restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldPassUserValidationWithCurrentDate() {
		User user = new User(0, "email@gmail.com", "login", "name", LocalDate.now());
		assertEquals(user, restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldNotPassUserValidationWithFutureDay() {
		User user = new User(0, "email@gmail.com", "login", "name", LocalDate.now().plusDays(1));
		assertNull(restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldNotPassFilmValidationWithEmptyName() {
		Film film = new Film(0, "", "desc", LocalDate.of(1912, 12, 28),
				Duration.ofHours(2));
		assertNull(restTemplate.postForObject(url + "/films", film, Film.class));
	}

	@Test
	void shouldPassFilmValidationWith200DescLength() {
		Film film = new Film(0, "name", "d".repeat(200), LocalDate.of(1912, 12, 28),
				Duration.ofHours(2));
		assertEquals(film, restTemplate.postForObject(url + "/films", film, Film.class));
	}

	@Test
	void shouldNotPassFilmValidationWith201DescLength() {
		Film film = new Film(0, "name", "d".repeat(201), LocalDate.of(1912, 12, 28),
				Duration.ofHours(2));
		assertNull(restTemplate.postForObject(url + "/films", film, Film.class));
	}

	@Test
	void shouldPassFilmValidationWith199DescLength() {
		Film film = new Film(0, "name", "d".repeat(199), LocalDate.of(1912, 12, 28),
				Duration.ofHours(2));
		assertEquals(film, restTemplate.postForObject(url + "/films", film, Film.class));
	}

	@Test
	void shouldNotPassFilmValidationWithNegativeDuration() {
		Film film = new Film(0, "name", "desc", LocalDate.of(1912, 12, 28),
				Duration.ofHours(-1));
		assertNull(restTemplate.postForObject(url + "/films", film, Film.class));
	}

	@Test
	void shouldPassFilmValidationWithZeroDuration() {
		Film film = new Film(0, "name", "desc", LocalDate.of(1912, 12, 28),
				Duration.ofHours(0));
		assertEquals(film, restTemplate.postForObject(url + "/films", film, Film.class));
	}

	@Test
	void shouldNotPassFilmValidationWithReleaseDateBefore1895_12_28() {
		Film film = new Film(0, "name", "desc", LocalDate.of(1800, 12, 28),
				Duration.ofHours(0));
		assertNull(restTemplate.postForObject(url + "/films", film, Film.class));
	}

	@Test
	void shouldPassFilmValidationWithReleaseDate1895_12_28() {
		Film film = new Film(0, "name", "desc", LocalDate.of(1895, 12, 28),
				Duration.ofHours(0));
		assertEquals(film, restTemplate.postForObject(url + "/films", film, Film.class));
	}

}
