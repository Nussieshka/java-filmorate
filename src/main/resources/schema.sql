CREATE TABLE IF NOT EXISTS rating(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS movies(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    description VARCHAR(255),
    release_date TIMESTAMP,
    duration INT,
    rating_id INT REFERENCES rating(id)
);

CREATE TABLE IF NOT EXISTS genre(
     id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
     name VARCHAR
);

CREATE TABLE IF NOT EXISTS movie_genres(
    movie_id INT REFERENCES movies(id),
    genre_id INT REFERENCES genre(id)
);

CREATE TABLE IF NOT EXISTS users(
    id INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email VARCHAR NOT NULL,
    login VARCHAR NOT NULL,
    name VARCHAR,
    birthday TIMESTAMP
);

CREATE TABLE IF NOT EXISTS movie_likes(
    movie_id INT REFERENCES movies(id),
    user_id INT REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS friends(
    first_user_id INT REFERENCES users(id),
    second_user_id INT REFERENCES users(id),
    status VARCHAR
);