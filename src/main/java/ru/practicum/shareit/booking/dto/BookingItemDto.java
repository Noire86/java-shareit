package ru.practicum.shareit.booking.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class BookingItemDto {
    private Integer id;
    private Integer bookerId;
}
