package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.yandex.practicum.filmorate.util.FriendStatus;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@Jacksonized
public class User {

    private Long id;

    @Builder.Default
    private final Map<Long, FriendStatus> friends = new HashMap<>();

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;

    public void addFriend(Long id, FriendStatus friendStatus) {
        friends.put(id, friendStatus);
    }

    public void removeFriend(Long id) {
        friends.remove(id);
    }
}
