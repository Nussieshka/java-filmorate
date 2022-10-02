package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private Integer id;

    @NotEmpty
    private String name;

    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    private Duration duration;

    public long getDuration() {
        return duration.getSeconds();
    }
}
