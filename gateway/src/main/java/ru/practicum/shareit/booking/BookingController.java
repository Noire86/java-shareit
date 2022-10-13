package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.exception.model.ValidationException;
import ru.practicum.shareit.util.State;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @Validated @RequestBody BookingCreationDto bookingCreationDto) {

        log.info("Creating booking {}, userId={}", bookingCreationDto, userId);
        return bookingClient.create(userId, bookingCreationDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBookingById(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer bookingId) {

        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.findBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestorBookings(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        State state = State.from(stateParam)
                .orElseThrow(() -> new ValidationException("Unknown state: " + stateParam, HttpStatus.BAD_REQUEST));
        log.info("Get requestor bookings with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);

        return bookingClient.getRequestorBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(name = "state", defaultValue = "all") String stateParam,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {

        State state = State.from(stateParam)
                .orElseThrow(() -> new ValidationException("Unknown state: " + stateParam, HttpStatus.BAD_REQUEST));
        log.info("Get owner bookings with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);

        return bookingClient.getOwnerBookings(userId, state, from, size);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveRequest(
            @RequestHeader("X-Sharer-User-Id") Integer userId,
            @PathVariable Integer bookingId,
            @RequestParam Boolean approved) {

        if (approved == null) throw new ValidationException("Invalid approved param", HttpStatus.BAD_REQUEST);
        log.info("approving request for bookingId={} and userId={}", bookingId, userId);
        return bookingClient.approveRequest(userId, bookingId, approved);
    }
}
