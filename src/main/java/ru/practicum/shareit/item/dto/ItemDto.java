package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

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
