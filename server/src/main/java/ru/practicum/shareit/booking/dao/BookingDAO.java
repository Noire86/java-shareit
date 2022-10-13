package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.util.Status;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для бронирований
 */
public interface BookingDAO extends JpaRepository<Booking, Integer> {

    @Query("select bk from Booking bk join Item i on i.id = bk.item.id where i.owner = ?1 order by bk.start desc")
    List<Booking> findOwnerBookings(Integer ownerId, Pageable pageable);

    @Query("select bk from Booking bk join Item i on i.id = bk.item.id where i.owner = ?1 and bk.status = ?2 " +
            "order by bk.start desc")
    List<Booking> findOwnerBookingsByStatus(Integer ownerId, Status status, Pageable pageable);

    @Query("select bk from Booking bk join Item i on i.id = bk.item.id where i.owner = ?1 and bk.end < ?2 " +
            "order by bk.start desc")
    List<Booking> findOwnerBookingsFromPast(Integer ownerId, LocalDateTime dateTime, Pageable pageable);

    @Query("select bk from Booking bk join Item i on i.id = bk.item.id where i.owner = ?1 and bk.start > ?2 " +
            "order by bk.start desc")
    List<Booking> findOwnerBookingsFromFuture(Integer ownerId, LocalDateTime dateTime, Pageable pageable);

    @Query("select bk from Booking bk join Item i on i.id = bk.item.id where i.owner = ?1 and bk.start < ?2 and bk.end > ?2 " +
            "order by bk.start desc")
    List<Booking> findOwnerCurrentBookings(Integer ownerId, LocalDateTime dateTime, Pageable pageable);

    @Query("select bk from Booking bk where bk.booker.id = ?1 order by bk.start desc")
    List<Booking> findBookerBookings(Integer bookerId, Pageable pageable);

    @Query("select bk from Booking bk where bk.booker.id = ?1 and bk.status = ?2 order by bk.start desc")
    List<Booking> findBookerBookingsByStatus(Integer bookerId, Status status, Pageable pageable);

    @Query("select bk from Booking bk where bk.booker.id = ?1 and bk.end < ?2 order by bk.start desc")
    List<Booking> findBookerBookingsFromPast(Integer ownerId, LocalDateTime dateTime, Pageable pageable);

    @Query("select bk from Booking bk where bk.booker.id = ?1 and bk.start > ?2 order by bk.start desc")
    List<Booking> findBookerBookingsFromFuture(Integer ownerId, LocalDateTime dateTime, Pageable pageable);

    @Query("select bk from Booking bk where bk.booker.id = ?1 and bk.start < ?2 and bk.end > ?2 order by bk.start desc")
    List<Booking> findBookerCurrentBookings(Integer ownerId, LocalDateTime dateTime, Pageable pageable);

    @Query(value = "SELECT * FROM bookings AS bk " +
            "WHERE bk.booker_id = ? " +
            "AND bk.item_id = ? " +
            "AND bk.status = 'APPROVED' " +
            "AND bk.end_date < NOW()",
            nativeQuery = true)
    List<Booking> findValidUserBookingsByItem(Integer userId, Integer itemId);

    @Query(value = "SELECT * FROM bookings AS bk JOIN items AS i ON bk.item_id = i.id " +
            "WHERE bk.item_id = ? AND bk.start_date > NOW() AND bk.status = 'APPROVED' " +
            "ORDER BY bk.start_date ASC LIMIT 1", nativeQuery = true)
    List<Booking> getNextBooking(Integer itemId);

    @Query(value = "SELECT * FROM bookings bk JOIN items AS i ON i.id = bk.item_id " +
            "WHERE bk.item_id = ? AND bk.end_date < NOW() AND bk.status = 'APPROVED'" +
            "ORDER BY bk.end_date ASC LIMIT 1", nativeQuery = true)
    List<Booking> getLastBooking(Integer itemId);

}

