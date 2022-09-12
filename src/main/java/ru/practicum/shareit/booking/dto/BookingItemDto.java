package ru.practicum.shareit.booking.dto;

import lombok.*;

/**
 * DTO для отображения бронирования в объекте вещи
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BookingItemDto {
    private Integer id;
    private Integer bookerId;
}
