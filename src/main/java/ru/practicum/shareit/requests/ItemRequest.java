package ru.practicum.shareit.requests;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * // TODO .
 */
@Value
@Builder(toBuilder = true)
public class ItemRequest {
    Integer id;
    String description;
    User requestor;
    LocalDateTime created;
}
