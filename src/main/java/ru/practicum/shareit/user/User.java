package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */
@Value
@Builder(toBuilder = true)
public class User {
    Integer id;
    @NotNull
    String name;
    @Email
    String email;
}
