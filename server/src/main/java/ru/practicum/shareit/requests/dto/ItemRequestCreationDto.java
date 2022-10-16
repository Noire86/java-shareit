package ru.practicum.shareit.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.requests.model.ItemRequest;

/**
 * A DTO for the {@link ItemRequest} entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

public class ItemRequestCreationDto {
    private String description;
}