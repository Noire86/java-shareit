package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.util.State;
import ru.practicum.shareit.util.Status;

import java.util.List;

/**
 * Сервисный интерфейс бронирования
 */
public interface BookingService {

    BookingDto create(Integer userId, BookingCreationDto bookingDto);
    BookingDto findBookingById(Integer userId, Integer bookingId);
    List<BookingDto> getRequestorBookings(Integer userId, String state);
    List<BookingDto> getOwnerBookings(Integer userId, String state);
    BookingDto approveRequest(Integer userId, Integer bookingId, Boolean isApproved);
}
