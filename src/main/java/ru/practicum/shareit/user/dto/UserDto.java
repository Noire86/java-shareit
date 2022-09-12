package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;

/**
 * ДТО-модель пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class UserDto {
    private Integer id;
    private String name;
    @Email
    private String email;
}
