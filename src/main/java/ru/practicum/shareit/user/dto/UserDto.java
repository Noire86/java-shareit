package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;

/**
 * ДТО-модель пользователя
 */
@Value
@Builder(toBuilder = true)
public class UserDto {
    Integer id;
    String name;
    @Email
    String email;
}
