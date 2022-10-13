package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

/**
 * Сервисный интерфейс бронирования
 */
public interface BookingService {

    BookingDto create(Integer userId, BookingCreationDto bookingDto);

    BookingDto findBookingById(Integer userId, Integer bookingId);

    List<BookingDto> getRequestorBookings(Integer userId, String state, Integer from, Integer size);

    List<BookingDto> getOwnerBookings(Integer userId, String state, Integer from, Integer size);

    BookingDto approveRequest(Integer userId, Integer bookingId, Boolean isApproved);
}
