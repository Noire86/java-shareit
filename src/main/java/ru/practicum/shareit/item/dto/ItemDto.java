package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * // TODO .
 */

@Value
@Builder(toBuilder = true)
public class ItemDto {
    Integer id;
    String name;
    String description;
    Boolean available;
    String request;
}
