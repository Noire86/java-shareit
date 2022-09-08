package ru.practicum.shareit.booking;

import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;

/**
 * // Ожидает реализации
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto create(@RequestHeader(HeaderKey.USER_KEY) Long userId, @Valid @RequestBody BookingCreatingDto bookingCreatingDto) {

    }

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(HeaderKey.USER_KEY) Long userId, @PathVariable Long bookingId)

    }


    @GetMapping()
    public List<BookingDto> getAllBookingsForRequester(@RequestHeader(HeaderKey.USER_KEY) Long userId,
                                                       @RequestParam(defaultValue = "ALL") String state) {

    }

    @GetMapping("/owner")
    public List<BookingDto> getAllBookingsForOwner(@RequestHeader(HeaderKey.USER_KEY) Long userId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
    }

    @PatchMapping("/{bookingId}")
    public BookingDto update(@RequestHeader(HeaderKey.USER_KEY) Long userId, @PathVariable Long bookingId,
                             @RequestParam Boolean approved) {
    }



}
