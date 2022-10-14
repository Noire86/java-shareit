package java.ru.practicum.shareit.booking.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.booking.dao.BookingDAO;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.util.Status;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class BookingDAOTest extends BaseTest {

    @Autowired
    private BookingDAO bookingDAO;

    @Autowired
    protected TestEntityManager em;

    @BeforeEach
    void setupEntities() {
        user = em.persistFlushFind(prepareDetachedUser());
        booker = em.persistFlushFind(prepareDetachedUser());
        dummy = em.persistFlushFind(prepareDetachedUser());

        item = em.persistFlushFind(prepareDetachedItem(user, true));
        item2 = em.persistFlushFind(prepareDetachedItem(user, true));
        unavailable = em.persistFlushFind(prepareDetachedItem(user, false));

        booking = em.persistFlushFind(prepareDetachedBooking(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(5),
                booker,
                item,
                Status.WAITING));

        booking2 = em.persistFlushFind(prepareDetachedBooking(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(5),
                booker,
                item2,
                Status.REJECTED));

        futureBooking = em.persistFlushFind(prepareDetachedBooking(
                LocalDateTime.now().plusDays(4),
                LocalDateTime.now().plusDays(5),
                booker,
                item,
                Status.APPROVED));

        pastBooking = em.persistFlushFind(prepareDetachedBooking(
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(3),
                booker,
                item,
                Status.APPROVED));
    }

    @Test
    void findOwnerBookings() {
        List<Booking> bookings = bookingDAO.findOwnerBookings(user.getId(), Pageable.unpaged());
        assertEquals(4, bookings.size());
        assertTrue(bookings.contains(booking));
        assertTrue(bookings.contains(booking2));
        assertTrue(bookings.contains(pastBooking));
        assertTrue(bookings.contains(futureBooking));
    }

    @Test
    void findOwnerBookingsByStatus() {
        List<Booking> waiting = bookingDAO.findOwnerBookingsByStatus(user.getId(), Status.WAITING, Pageable.unpaged());
        assertEquals(1, waiting.size());
        assertTrue(waiting.contains(booking));

        List<Booking> rejected = bookingDAO.findOwnerBookingsByStatus(user.getId(), Status.REJECTED, Pageable.unpaged());
        assertEquals(1, rejected.size());
        assertTrue(rejected.contains(booking2));

    }

    @Test
    void findOwnerBookingsFromPast() {
        List<Booking> past = bookingDAO.findOwnerBookingsFromPast(user.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertEquals(1, past.size());
        assertTrue(past.contains(pastBooking));
    }

    @Test
    void findOwnerBookingsFromFuture() {
        List<Booking> future = bookingDAO.findOwnerBookingsFromFuture(user.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertEquals(2, future.size());
        assertTrue(future.contains(booking));
        assertTrue(future.contains(futureBooking));
    }

    @Test
    void findOwnerCurrentBookings() {
        List<Booking> current = bookingDAO.findOwnerCurrentBookings(user.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertTrue(current.contains(booking2));
    }

    @Test
    void findBookerBookings() {
        List<Booking> bookings = bookingDAO.findBookerBookings(booker.getId(), Pageable.unpaged());
        assertEquals(4, bookings.size());
        assertTrue(bookings.contains(booking));
        assertTrue(bookings.contains(booking2));
        assertTrue(bookings.contains(pastBooking));
        assertTrue(bookings.contains(futureBooking));
    }

    @Test
    void findBookerBookingsByStatus() {
        List<Booking> waiting = bookingDAO.findBookerBookingsByStatus(booker.getId(), Status.WAITING, Pageable.unpaged());
        assertEquals(1, waiting.size());
        assertTrue(waiting.contains(booking));

        List<Booking> rejected = bookingDAO.findBookerBookingsByStatus(booker.getId(), Status.REJECTED, Pageable.unpaged());
        assertEquals(rejected.size(), 1);
        assertTrue(rejected.contains(booking2));
    }

    @Test
    void findBookerBookingsFromPast() {
        List<Booking> past = bookingDAO.findBookerBookingsFromPast(booker.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertEquals(1, past.size());
        assertTrue(past.contains(pastBooking));
    }

    @Test
    void findBookerBookingsFromFuture() {
        List<Booking> future = bookingDAO.findBookerBookingsFromFuture(booker.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertEquals(2, future.size());
        assertTrue(future.contains(booking));
        assertTrue(future.contains(futureBooking));
    }

    @Test
    void findBookerCurrentBookings() {
        List<Booking> current = bookingDAO.findBookerCurrentBookings(booker.getId(), LocalDateTime.now(), Pageable.unpaged());
        assertEquals(1, current.size());
        assertTrue(current.contains(booking2));
    }

    @Test
    void findValidUserBookingsByItem() {
        List<Booking> valid = bookingDAO.findValidUserBookingsByItem(booker.getId(), item.getId());
        assertEquals(1, valid.size());
        assertTrue(valid.contains(pastBooking));
    }

    @Test
    void getNextBooking() {
        List<Booking> nextBooking = bookingDAO.getNextBooking(item.getId());
        assertEquals(1, nextBooking.size());
        assertTrue(nextBooking.contains(futureBooking));
    }

    @Test
    void getLastBooking() {
        List<Booking> lastBooking = bookingDAO.getLastBooking(item.getId());
        assertEquals(1, lastBooking.size());
        assertTrue(lastBooking.contains(pastBooking));
    }
}