package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingDAO;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessViolationException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.util.PaginationUtils;
import ru.practicum.shareit.util.State;
import ru.practicum.shareit.util.Status;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для бронирований
 */
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO;
    private final UserDAO userDAO;
    private final ItemDAO itemDAO;

    @Override
    public BookingDto create(Integer userId, BookingCreationDto bookingCreationDto) {
        Booking booking = new Booking();
        User booker = userDAO.findById(userId).orElseThrow(EntityNotFoundException::new);
        Item item = itemDAO.findById(bookingCreationDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        LocalDateTime start = bookingCreationDto.getStart();
        LocalDateTime end = bookingCreationDto.getEnd();

        if (!item.getAvailable()) {
            throw new ValidationException(String.format("Item with ID %d is not available for booking!", item.getId()),
                    HttpStatus.BAD_REQUEST);
        }

        if (item.getOwner().equals(booker.getId())) {
            throw new ValidationException("Cannot create booking as an item owner", HttpStatus.NOT_FOUND);

        }

        if (start.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Booking date error: starting date time is in the past", HttpStatus.BAD_REQUEST);
        }

        if (end.isBefore(LocalDateTime.now())) {
            throw new ValidationException("Booking date error: ending date time is in the past", HttpStatus.BAD_REQUEST);
        }

        if (start.isAfter(end)) {
            throw new ValidationException("Booking date error: starting date is after the ending time", HttpStatus.BAD_REQUEST);
        }


        booking.setStart(start);
        booking.setEnd(end);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        bookingDAO.save(booking);

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto findBookingById(Integer userId, Integer bookingId) {
        Booking booking = bookingDAO.findById(bookingId).orElseThrow(EntityNotFoundException::new);
        User owner = userDAO.findById(booking.getItem().getOwner()).orElseThrow(EntityNotFoundException::new);
        User booker = booking.getBooker();

        if (!booker.getId().equals(userId) && !owner.getId().equals(userId)) {
            throw new AccessViolationException("You must be an owner or a respective booker to access this booking",
                    HttpStatus.NOT_FOUND);
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getRequestorBookings(Integer userId, String state, Integer from, Integer size) {
        User booker = userDAO.findById(userId).orElseThrow(EntityNotFoundException::new);
        Pageable pageable = PaginationUtils.handlePaginationParams(from, size);
        List<Booking> bookingList = new ArrayList<>();

        try {
            State stateEnum = State.valueOf(state);
            switch (stateEnum) {
                case ALL:
                    bookingList.addAll(bookingDAO.findBookerBookings(booker.getId(), pageable));
                    break;
                case PAST:
                    bookingList.addAll(bookingDAO.findBookerBookingsFromPast(booker.getId(), LocalDateTime.now(), pageable));
                    break;
                case FUTURE:
                    bookingList.addAll(bookingDAO.findBookerBookingsFromFuture(booker.getId(), LocalDateTime.now(), pageable));
                    break;
                case CURRENT:
                    bookingList.addAll(bookingDAO.findBookerCurrentBookings(booker.getId(), LocalDateTime.now(), pageable));
                    break;
                case WAITING:
                    bookingList.addAll(bookingDAO.findBookerBookingsByStatus(booker.getId(), Status.WAITING, pageable));
                    break;
                case REJECTED:
                    bookingList.addAll(bookingDAO.findBookerBookingsByStatus(booker.getId(), Status.REJECTED, pageable));
                    break;
            }
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(String.format("Unknown state: %s", state), HttpStatus.BAD_REQUEST);
        }

        return bookingList
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getOwnerBookings(Integer userId, String state, Integer from, Integer size) {
        User owner = userDAO.findById(userId).orElseThrow(EntityNotFoundException::new);
        Pageable pageable = PaginationUtils.handlePaginationParams(from, size);
        List<Booking> bookingList = new ArrayList<>();

        try {
            State stateEnum = State.valueOf(state);
            switch (stateEnum) {
                case ALL:
                    bookingList.addAll(bookingDAO.findOwnerBookings(owner.getId(), pageable));
                    break;
                case PAST:
                    bookingList.addAll(bookingDAO.findOwnerBookingsFromPast(owner.getId(), LocalDateTime.now(), pageable));
                    break;
                case FUTURE:
                    bookingList.addAll(bookingDAO.findOwnerBookingsFromFuture(owner.getId(), LocalDateTime.now(), pageable));
                    break;
                case CURRENT:
                    bookingList.addAll(bookingDAO.findOwnerCurrentBookings(owner.getId(), LocalDateTime.now(), pageable));
                    break;
                case WAITING:
                    bookingList.addAll(bookingDAO.findOwnerBookingsByStatus(owner.getId(), Status.WAITING, pageable));
                    break;
                case REJECTED:
                    bookingList.addAll(bookingDAO.findOwnerBookingsByStatus(owner.getId(), Status.REJECTED, pageable));
                    break;
            }
        } catch (IllegalArgumentException ex) {
            throw new ValidationException(String.format("Unknown state: %s", state), HttpStatus.BAD_REQUEST);
        }

        return bookingList
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto approveRequest(Integer userId, Integer bookingId, Boolean isApproved) {
        Booking booking = bookingDAO.findById(bookingId).orElseThrow(EntityNotFoundException::new);
        Item item = itemDAO.findById(booking.getItem().getId()).orElseThrow(EntityNotFoundException::new);
        User user = userDAO.findById(userId).orElseThrow(EntityNotFoundException::new);

        if (!user.getId().equals(item.getOwner())) {
            throw new ValidationException(
                    String.format("Item with ID %d does not belong to User with ID %d", item.getId(), user.getId()),
                    HttpStatus.NOT_FOUND);
        }

        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("This booking is already marked as APPROVED!", HttpStatus.BAD_REQUEST);
        }

        booking.setStatus(Boolean.TRUE.equals(isApproved) ? Status.APPROVED : Status.REJECTED);
        bookingDAO.save(booking);
        return BookingMapper.toBookingDto(booking);
    }
}
