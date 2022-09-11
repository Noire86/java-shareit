package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.Status;

import java.time.LocalDateTime;

/**
 * // Ожидает реализации
 */
@Value
@Builder(toBuilder = true)
public class BookingDto {
    Integer id;
    LocalDateTime start;
    LocalDateTime end;
    User booker;
    Item item;
    Status status;
}
