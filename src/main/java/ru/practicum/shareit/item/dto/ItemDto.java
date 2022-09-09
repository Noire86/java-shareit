package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * // DTO-модель для итема
 */

@Value
@Builder(toBuilder = true)
public class ItemDto {
    Integer id;
    String name;
    String description;
    Boolean available;
    Integer request;
}
