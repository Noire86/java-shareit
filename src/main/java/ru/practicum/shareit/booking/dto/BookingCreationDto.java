package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class BookingCreationDto {
    @NotNull
    Integer itemId;
    @NotNull
    LocalDateTime start;
    @NotNull
    LocalDateTime end;
}
