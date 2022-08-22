package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.validation.annotations.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Value
@Builder(toBuilder = true)
public class UserDto {
    Integer id;
    @NotNull
    String name;

    @UniqueEmail
    @Email
    @NotNull
    String email;
}
