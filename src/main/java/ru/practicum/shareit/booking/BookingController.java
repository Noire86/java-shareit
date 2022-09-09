package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.util.State;

import java.util.List;

/**
 * // Ожидает реализации
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> create(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Validated @RequestBody BookingCreationDto bookingCreationDto) {

        return new ResponseEntity<>(bookingService.create(userId, bookingCreationDto), HttpStatus.CREATED);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> findBookingById(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer bookingId) {

        return new ResponseEntity<>(bookingService.findBookingById(userId, bookingId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<BookingDto>> getRequestorBookings(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(defaultValue = "ALL") State state) {

        return new ResponseEntity<>(bookingService.getRequestorBookings(userId, state), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDto>> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @RequestParam(defaultValue = "ALL") State state) {

        return new ResponseEntity<>(bookingService.getOwnerBookings(userId, state), HttpStatus.OK);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> approveRequest(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer bookingId,
            @RequestParam Boolean approved) {

        return new ResponseEntity<>(bookingService.approveRequest(userId, bookingId, approved), HttpStatus.OK);
    }
}