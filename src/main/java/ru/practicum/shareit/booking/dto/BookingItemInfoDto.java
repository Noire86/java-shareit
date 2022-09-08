package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BookingItemInfoDto {
    Integer id;
    Integer bookerId;
}
