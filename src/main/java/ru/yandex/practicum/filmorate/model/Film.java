package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@Jacksonized
public class Film {
    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    private Duration duration;

    @Builder.Default
    private final Set<Long> likedBy = new HashSet<>();

    public long getDuration() {
        return duration.getSeconds();
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
