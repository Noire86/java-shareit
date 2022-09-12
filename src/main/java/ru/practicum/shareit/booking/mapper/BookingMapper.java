package ru.practicum.shareit.booking.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.dao.UserDAO;

/**
 * Маппер для DTO классов бронирования
 */
@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final UserDAO userDAO;

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .status(booking.getStatus())
                .build();
    }

    public static BookingCreationDto toBookingCreationDto(Booking booking) {
        return BookingCreationDto.builder()
                .itemId(booking.getItem().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .build();
    }

    public static BookingItemDto toBookingItemInfoDto(Booking booking) {
        return BookingItemDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBooker().getId())
                .build();
    }
}
