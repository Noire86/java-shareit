package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.util.Status;

import java.util.List;

public interface BookingService {

    BookingDto create(Integer userId, BookingDto bookingDto);
    BookingDto findBookingById(Integer userId, Integer bookingId);
    List<Booking> getRequestorBookings(Integer userId, Status status);
    List<Booking> getOwnerBookings(Integer userId, Status status);
    BookingDto approveRequest(Integer userId, Integer bookingId, Boolean isApproved);
}
