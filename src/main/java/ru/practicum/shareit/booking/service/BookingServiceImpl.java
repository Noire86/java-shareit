package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.util.State;
import ru.practicum.shareit.util.Status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO;
    private final UserDAO userDAO;
    private final ItemDAO itemDAO;

    @Override
    public BookingDto create(Integer userId, BookingCreationDto bookingCreationDto) {
        Booking booking = new Booking();
        User booker = userDAO.getReferenceById(userId);
        Item item = itemDAO.getReferenceById(bookingCreationDto.getItemId());
        LocalDateTime start = bookingCreationDto.getStart();
        LocalDateTime end = bookingCreationDto.getEnd();

        if (!item.getAvailable()) {
            throw new ValidationException(String.format("Item with ID %d is not available for booking!", item.getId()),
                    HttpStatus.FORBIDDEN);
        }

        if (item.getOwner().equals(booker.getId())) {
            throw new ValidationException("Cannot create booking as an item owner", HttpStatus.BAD_REQUEST);
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
        Booking booking = bookingDAO.getReferenceById(bookingId);
        User user = userDAO.getReferenceById(userId);
        User booker = booking.getBooker();

        if (!user.getId().equals(userId) && !booker.getId().equals(userId)) {
            throw new AccessViolationException("You must be an owner or a respective booker to access this booking",
                    HttpStatus.FORBIDDEN);
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getRequestorBookings(Integer userId, State state) {
        User booker = userDAO.getReferenceById(userId);
        List<Booking> bookingList = new ArrayList<>();

        switch (state) {
            case ALL:
                bookingList.addAll(bookingDAO.findBookerBookings(booker.getId()));
                break;
            case PAST:
                bookingList.addAll(bookingDAO.findBookerBookingsFromPast(booker.getId(), LocalDateTime.now()));
                break;
            case FUTURE:
                bookingList.addAll(bookingDAO.findBookerBookingsFromFuture(booker.getId(), LocalDateTime.now()));
                break;
            case CURRENT:
                bookingList.addAll(bookingDAO.findBookerCurrentBookings(booker.getId(), LocalDateTime.now()));
                break;
            case WAITING:
                bookingList.addAll(bookingDAO.findBookerBookingsByStatus(booker.getId(), Status.WAITING));
                break;
            case REJECTED:
                bookingList.addAll(bookingDAO.findBookerBookingsByStatus(booker.getId(), Status.REJECTED));
                break;

            default:
                throw new ValidationException(
                        String.format("Error: the state %s is not supported in this request!", state.name()),
                        HttpStatus.BAD_REQUEST);
        }

        return bookingList
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getOwnerBookings(Integer userId, State state) {
        User owner = userDAO.getReferenceById(userId);
        List<Booking> bookingList = new ArrayList<>();

        switch (state) {
            case ALL:
                bookingList.addAll(bookingDAO.findOwnerBookings(owner.getId()));
                break;
            case PAST:
                bookingList.addAll(bookingDAO.findOwnerBookingsFromPast(owner.getId(), LocalDateTime.now()));
                break;
            case FUTURE:
                bookingList.addAll(bookingDAO.findOwnerBookingsFromFuture(owner.getId(), LocalDateTime.now()));
                break;
            case CURRENT:
                bookingList.addAll(bookingDAO.findOwnerCurrentBookings(owner.getId(), LocalDateTime.now()));
                break;
            case WAITING:
                bookingList.addAll(bookingDAO.findOwnerBookingsByStatus(owner.getId(), Status.WAITING));
                break;
            case REJECTED:
                bookingList.addAll(bookingDAO.findOwnerBookingsByStatus(owner.getId(), Status.REJECTED));
                break;

            default:
                throw new ValidationException(
                        String.format("Error: the state %s is not supported in this request!", state.name()),
                        HttpStatus.BAD_REQUEST);
        }

        return bookingList
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingDto approveRequest(Integer userId, Integer bookingId, Boolean isApproved) {
        Booking booking = bookingDAO.getReferenceById(bookingId);
        Item item = itemDAO.getReferenceById(booking.getItem().getId());
        User user = userDAO.getReferenceById(userId);

        if (!user.getId().equals(item.getOwner())) {
            throw new ValidationException(
                    String.format("Item with ID %d does not belong to User with ID %d", item.getId(), user.getId()),
                    HttpStatus.FORBIDDEN);
        }

        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("This booking is already marked as APPROVED!", HttpStatus.BAD_REQUEST);
        }

        booking.setStatus(isApproved ? Status.APPROVED : Status.REJECTED);
        bookingDAO.save(booking);
        return BookingMapper.toBookingDto(booking);
    }
}
