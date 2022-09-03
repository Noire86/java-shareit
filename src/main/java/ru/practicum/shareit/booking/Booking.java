package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.Status;

import java.time.LocalDateTime;

/**
 * // Модель бронирования
 */
@Value
@Builder(toBuilder = true)
public class Booking {
    Integer id;
    LocalDateTime start;
    LocalDateTime end;
    Item item;
    User booker;
    Status status;
}
