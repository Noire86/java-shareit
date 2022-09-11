package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BookingItemDto {
    Integer id;
    Integer bookerId;
}
