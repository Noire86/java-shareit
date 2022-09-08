package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.util.Status;

import java.util.List;

public class BookingServiceImpl implements BookingService {

    @Override
    public BookingDto create(Integer userId, BookingDto bookingDto) {
        return null;
    }

    @Override
    public BookingDto findBookingById(Integer userId, Integer bookingId) {
        return null;
    }

    @Override
    public List<Booking> getRequestorBookings(Integer userId, Status status) {
        return null;
    }

    @Override
    public List<Booking> getOwnerBookings(Integer userId, Status status) {
        return null;
    }

    @Override
    public BookingDto approveRequest(Integer userId, Integer bookingId, Boolean isApproved) {
        return null;
    }
}
