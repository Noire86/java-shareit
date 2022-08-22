package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.validation.annotations.UniqueEmail;

/**
 * // TODO .
 */
@Value
@Builder(toBuilder = true)
public class User {
    Integer id;
    String name;
    @UniqueEmail
    String email;
}
