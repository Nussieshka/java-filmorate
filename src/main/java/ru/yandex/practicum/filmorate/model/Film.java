package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter @EqualsAndHashCode
public class Film {
    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    private Duration duration;

    private Set<Long> likedBy;

    public long getDuration() {
        return duration.getSeconds();
    }

    public Film() {
        this.likedBy = new HashSet<>();
    }

    public Film(String name, String description, LocalDate releaseDate, Duration duration) {
        this();
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public int getLikesCount() {
        return likedBy.size();
    }

    public void like(long id) {
        likedBy.add(id);
    }

    public void dislike(long id) {
        likedBy.remove(id);
    }
}
