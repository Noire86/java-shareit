package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.Status;

import java.time.LocalDateTime;

/**
 * Основной DTO для бронирования
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BookingDto {
    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private User booker;
    private Item item;
    private Status status;
}
