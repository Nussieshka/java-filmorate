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
		User user = createUser("email@gmail.com", "login", "name", LocalDate.now().minusDays(15));
		User restTemplateUser = restTemplate.postForObject(url + "/users", user, User.class);
		user.setId(restTemplateUser.getId());
		assertEquals(user, restTemplateUser);
	}

	@Test
	void shouldPassFilmValidation() {
		Film film = createFilm("film", "desc", LocalDate.of(1912, 12, 28),
				Duration.ofHours(2));
		Film restTemplateFilm = restTemplate.postForObject(url + "/films", film, Film.class);
		film.setId(restTemplateFilm.getId());
		assertEquals(film, restTemplateFilm);
	}

	@Test
	void shouldNotPassUserValidationWithEmptyLogin() {
		User user = createUser("email@gmail.com", "", "name", LocalDate.now().minusDays(15));
		assertNull(restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldNotPassUserValidationWithWrongEmail() {
		User user = createUser("email", "login", "name", LocalDate.now().minusDays(15));
		assertNull(restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldNotPassUserValidationWithEmptyEmail() {
		User user = createUser("", "login", "name", LocalDate.now().minusDays(15));
		assertNull(restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldPassUserValidationWithEmptyName() {
		User user = createUser("email@gmail.com", "login", "", LocalDate.now().minusDays(15));
		User restTemplateUser = restTemplate.postForObject(url + "/users", user, User.class);
		user.setId(restTemplateUser.getId());
		assertEquals(user, restTemplateUser);
	}

	@Test
	void shouldNotPassUserValidationWithBlankLogin() {
		User user = createUser("email@gmail.com", " ", "name", LocalDate.now().minusDays(15));
		assertNull(restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldPassUserValidationWithCurrentDate() {
		User user = createUser("email@gmail.com", "login", "name", LocalDate.now());
		User restTemplateUser = restTemplate.postForObject(url + "/users", user, User.class);
		user.setId(restTemplateUser.getId());
		assertEquals(user, restTemplateUser);
	}

	@Test
	void shouldNotPassUserValidationWithFutureDay() {
		User user = createUser("email@gmail.com", "login", "name", LocalDate.now().plusDays(1));
		assertNull(restTemplate.postForObject(url + "/users", user, User.class));
	}

	@Test
	void shouldNotPassFilmValidationWithEmptyName() {
		Film film = createFilm("", "desc", LocalDate.of(1912, 12, 28),
				Duration.ofHours(2));
		assertNull(restTemplate.postForObject(url + "/films", film, Film.class));
	}

	@Test
	void shouldPassFilmValidationWith200DescLength() {
		Film film = createFilm("name", "d".repeat(200), LocalDate.of(1912, 12, 28),
				Duration.ofHours(2));
		Film restTemplateFilm = restTemplate.postForObject(url + "/films", film, Film.class);
		film.setId(restTemplateFilm.getId());
		assertEquals(film, restTemplateFilm);
	}

	@Test
	void shouldNotPassFilmValidationWith201DescLength() {
		Film film = createFilm("name", "d".repeat(201), LocalDate.of(1912, 12, 28),
				Duration.ofHours(2));
		assertNull(restTemplate.postForObject(url + "/films", film, Film.class));
	}

	@Test
	void shouldPassFilmValidationWith199DescLength() {
		Film film = createFilm("name", "d".repeat(199), LocalDate.of(1912, 12, 28),
				Duration.ofHours(2));
		Film restTemplateFilm = restTemplate.postForObject(url + "/films", film, Film.class);
		film.setId(restTemplateFilm.getId());
		assertEquals(film, restTemplateFilm);
	}

	@Test
	void shouldNotPassFilmValidationWithNegativeDuration() {
		Film film = createFilm("name", "desc", LocalDate.of(1912, 12, 28),
				Duration.ofHours(-1));
		assertNull(restTemplate.postForObject(url + "/films", film, Film.class));
	}

	@Test
	void shouldPassFilmValidationWithZeroDuration() {
		Film film = createFilm("name", "desc", LocalDate.of(1912, 12, 28),
				Duration.ofHours(0));
		Film restTemplateFilm = restTemplate.postForObject(url + "/films", film, Film.class);
		film.setId(restTemplateFilm.getId());
		assertEquals(film, restTemplateFilm);
	}

	@Test
	void shouldNotPassFilmValidationWithReleaseDateBefore1895_12_28() {
		Film film = createFilm("name", "desc", LocalDate.of(1800, 12, 28),
				Duration.ofHours(0));
		assertNull(restTemplate.postForObject(url + "/films", film, Film.class));
	}

	@Test
	void shouldPassFilmValidationWithReleaseDate1895_12_28() {
		Film film = createFilm("name", "desc", LocalDate.of(1895, 12, 28),
				Duration.ofHours(0));
		Film restTemplateFilm = restTemplate.postForObject(url + "/films", film, Film.class);
		film.setId(restTemplateFilm.getId());
		assertEquals(film, restTemplateFilm);
	}

	private User createUser(String email, String login, String name, LocalDate birthday) {
		User user = new User();
		user.setEmail(email);
		user.setLogin(login);
		user.setName(name);
		user.setBirthday(birthday);
		return user;
	}

	private Film createFilm(String name, String desc, LocalDate releaseDate, Duration duration) {
		Film film = new Film();
		film.setName(name);
		film.setDescription(desc);
		film.setReleaseDate(releaseDate);
		film.setDuration(duration);
		return film;
	}

}
