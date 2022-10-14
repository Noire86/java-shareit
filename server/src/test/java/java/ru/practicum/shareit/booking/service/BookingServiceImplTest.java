package java.ru.practicum.shareit.booking.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.booking.dao.BookingDAO;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.AccessViolationException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.util.PaginationUtils;
import ru.practicum.shareit.util.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookingServiceImplTest extends BaseTest {

    @MockBean
    private BookingDAO bookingDAO;

    @MockBean
    private UserDAO userDAO;

    @MockBean
    private ItemDAO itemDAO;

    @Autowired
    private BookingService bookingService;

    @BeforeEach
    private void setupEntities() {
        user = prepareDetachedUser();
        user.setId(1);

        booker = prepareDetachedUser();
        booker.setId(2);

        dummy = prepareDetachedUser();
        dummy.setId(3);

        item = prepareDetachedItem(user, true);
        item.setId(1);

        item2 = prepareDetachedItem(user, true);
        item2.setId(3);

        unavailable = prepareDetachedItem(user, false);
        unavailable.setId(2);

        booking = prepareDetachedBooking(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(5),
                booker,
                item,
                Status.WAITING);
        booking.setId(1);

        booking2 = prepareDetachedBooking(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(5),
                booker,
                item2,
                Status.REJECTED);
        booking2.setId(2);
    }

    @BeforeEach
    void setupMocks() {
        when(userDAO.findById(1))
                .thenReturn(Optional.of(user));

        when(userDAO.findById(2))
                .thenReturn(Optional.of(booker));

        when(userDAO.findById(3))
                .thenReturn(Optional.of(dummy));

        when(itemDAO.findById(eq(1)))
                .thenReturn(Optional.of(item));

        when(itemDAO.findById(eq(2)))
                .thenReturn(Optional.of(unavailable));

        when(bookingDAO.findById(1))
                .thenReturn(Optional.of(booking));
    }

    @Test
    void createValidBooking() {
        BookingCreationDto creationDto = BookingCreationDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(5))
                .build();

        BookingDto result = bookingService.create(2, creationDto);

        assertThat(result, Matchers.equalTo(BookingDto.builder()
                .start(creationDto.getStart())
                .end(creationDto.getEnd())
                .item(item)
                .booker(booker)
                .status(Status.WAITING)
                .build()));

    }

    @Test
    void createUnavailableItemBooking() {
        BookingCreationDto creationDto = BookingCreationDto.builder()
                .itemId(2)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(5))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> bookingService.create(2, creationDto));
        assertEquals("Item with ID 2 is not available for booking!", ex.getMessage());

    }

    @Test
    void createBookingAsAnOwner() {
        BookingCreationDto creationDto = BookingCreationDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(5))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> bookingService.create(1, creationDto));
        assertEquals("Cannot create booking as an item owner", ex.getMessage());

    }

    @Test
    void createBookingWithStartTimeInThePast() {
        BookingCreationDto creationDto = BookingCreationDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().minusDays(1))
                .end(LocalDateTime.now().plusDays(5))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> bookingService.create(2, creationDto));
        assertEquals("Booking date error: starting date time is in the past", ex.getMessage());

    }

    @Test
    void createBookingWithEndTimeInThePast() {
        BookingCreationDto creationDto = BookingCreationDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().minusDays(5))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> bookingService.create(2, creationDto));
        assertEquals("Booking date error: ending date time is in the past", ex.getMessage());

    }

    @Test
    void createBookingWithStartIsAfterEnd() {
        BookingCreationDto creationDto = BookingCreationDto.builder()
                .itemId(1)
                .start(LocalDateTime.now().plusDays(5))
                .end(LocalDateTime.now().plusDays(1))
                .build();

        ValidationException ex = assertThrows(ValidationException.class, () -> bookingService.create(2, creationDto));
        assertEquals("Booking date error: starting date is after the ending time", ex.getMessage());

    }

    @Test
    void findBookingByIdAsOwnerOrBooker() {
        assertEquals(bookingService.findBookingById(1, 1), BookingMapper.toBookingDto(booking));
        assertEquals(bookingService.findBookingById(2, 1), BookingMapper.toBookingDto(booking));
    }

    @Test
    void findBookingByIdByOtherUser() {
        AccessViolationException ex =
                assertThrows(AccessViolationException.class, () -> bookingService.findBookingById(3, 1));
        assertEquals(ex.getMessage(), "You must be an owner or a respective booker to access this booking");
    }

    @Test
    void getAllRequestorBookings() {
        when(bookingDAO.findBookerBookings(2, PaginationUtils.handlePaginationParams(0, 1)))
                .thenReturn(List.of(booking, booking2));

        assertEquals(bookingService.getRequestorBookings(2, "ALL", 0, 1),
                List.of(
                        BookingMapper.toBookingDto(booking),
                        BookingMapper.toBookingDto(booking2)
                ));
    }

    @Test
    void getPastRequestorBookings() {
        Booking pastBooking = new Booking();

        when(bookingDAO.findBookerBookingsFromPast(eq(2), any(), eq(PaginationUtils.handlePaginationParams(0, 1))))
                .thenReturn(List.of(pastBooking));

        assertEquals(bookingService.getRequestorBookings(2, "PAST", 0, 1), List.of(BookingMapper.toBookingDto(pastBooking)));
    }

    @Test
    void getFutureRequestorBookings() {
        Booking futureBooking = new Booking();

        when(bookingDAO.findBookerBookingsFromFuture(eq(2), any(), eq(PaginationUtils.handlePaginationParams(0, 1))))
                .thenReturn(List.of(futureBooking));

        assertEquals(bookingService.getRequestorBookings(2, "FUTURE", 0, 1), List.of(BookingMapper.toBookingDto(futureBooking)));
    }

    @Test
    void getCurrentRequestorBookings() {
        Booking currentBooking = new Booking();

        when(bookingDAO.findBookerCurrentBookings(eq(2), any(), eq(PaginationUtils.handlePaginationParams(0, 1))))
                .thenReturn(List.of(currentBooking));

        assertEquals(bookingService.getRequestorBookings(2, "CURRENT", 0, 1), List.of(BookingMapper.toBookingDto(currentBooking)));
    }

    @Test
    void getWaitingRequestorBookings() {
        when(bookingDAO.findBookerBookingsByStatus(eq(2), eq(Status.WAITING), eq(PaginationUtils.handlePaginationParams(0, 1))))
                .thenReturn(List.of(booking));

        assertEquals(bookingService.getRequestorBookings(2, "WAITING", 0, 1), List.of(BookingMapper.toBookingDto(booking)));
    }

    @Test
    void getRejectedRequestorBookings() {
        when(bookingDAO.findBookerBookingsByStatus(eq(2), eq(Status.REJECTED), eq(PaginationUtils.handlePaginationParams(0, 1))))
                .thenReturn(List.of(booking2));

        assertEquals(bookingService.getRequestorBookings(2, "REJECTED", 0, 1), List.of(BookingMapper.toBookingDto(booking2)));
    }

    @Test
    void getInvalidStateRequestorBookings() {
        ValidationException ex = assertThrows(ValidationException.class, () -> bookingService.getRequestorBookings(2, "UNKNOWN_STATE", 0, 1));
        assertEquals(ex.getMessage(), "Unknown state: UNKNOWN_STATE");
    }


    @Test
    void getAllOwnerBookings() {
        when(bookingDAO.findOwnerBookings(2, PaginationUtils.handlePaginationParams(0, 1)))
                .thenReturn(List.of(booking, booking2));

        assertEquals(bookingService.getOwnerBookings(2, "ALL", 0, 1),
                List.of(
                        BookingMapper.toBookingDto(booking),
                        BookingMapper.toBookingDto(booking2)
                ));
    }

    @Test
    void getPastOwnerBookings() {
        Booking pastBooking = new Booking();

        when(bookingDAO.findOwnerBookingsFromPast(eq(2), any(), eq(PaginationUtils.handlePaginationParams(0, 1))))
                .thenReturn(List.of(pastBooking));

        assertEquals(bookingService.getOwnerBookings(2, "PAST", 0, 1), List.of(BookingMapper.toBookingDto(pastBooking)));
    }

    @Test
    void getFutureOwnerBookings() {
        Booking futureBooking = new Booking();

        when(bookingDAO.findOwnerBookingsFromFuture(eq(2), any(), eq(PaginationUtils.handlePaginationParams(0, 1))))
                .thenReturn(List.of(futureBooking));

        assertEquals(bookingService.getOwnerBookings(2, "FUTURE", 0, 1), List.of(BookingMapper.toBookingDto(futureBooking)));
    }

    @Test
    void getCurrentOwnerBookings() {
        Booking currentBooking = new Booking();

        when(bookingDAO.findOwnerCurrentBookings(eq(2), any(), eq(PaginationUtils.handlePaginationParams(0, 1))))
                .thenReturn(List.of(currentBooking));

        assertEquals(bookingService.getOwnerBookings(2, "CURRENT", 0, 1), List.of(BookingMapper.toBookingDto(currentBooking)));
    }

    @Test
    void getWaitingOwnerBookings() {
        when(bookingDAO.findOwnerBookingsByStatus(eq(2), eq(Status.WAITING), eq(PaginationUtils.handlePaginationParams(0, 1))))
                .thenReturn(List.of(booking));

        assertEquals(bookingService.getOwnerBookings(2, "WAITING", 0, 1), List.of(BookingMapper.toBookingDto(booking)));
    }

    @Test
    void getRejectedOwnerBookings() {
        when(bookingDAO.findOwnerBookingsByStatus(eq(2), eq(Status.REJECTED), eq(PaginationUtils.handlePaginationParams(0, 1))))
                .thenReturn(List.of(booking2));

        assertEquals(bookingService.getOwnerBookings(2, "REJECTED", 0, 1), List.of(BookingMapper.toBookingDto(booking2)));
    }

    @Test
    void getInvalidStateOwnerBookings() {
        ValidationException ex = assertThrows(ValidationException.class, () -> bookingService.getOwnerBookings(2, "UNKNOWN_STATE", 0, 1));
        assertEquals(ex.getMessage(), "Unknown state: UNKNOWN_STATE");
    }

    @Test
    void approveValidBookingRequest() {
        assertEquals(bookingService.approveRequest(1, 1, true).getStatus(), Status.APPROVED);
    }

    @Test
    void approveRequestNotAsOwner() {
        ValidationException ex = assertThrows(ValidationException.class, () -> bookingService.approveRequest(3, 1, true));
        assertEquals(ex.getMessage(), "Item with ID 1 does not belong to User with ID 3");
    }

    @Test
    void approveRequestThatIsAlreadyApproved() {
        booking.setStatus(Status.APPROVED);
        ValidationException ex = assertThrows(ValidationException.class, () -> bookingService.approveRequest(1, 1, true));
        assertEquals(ex.getMessage(), "This booking is already marked as APPROVED!");
    }
}