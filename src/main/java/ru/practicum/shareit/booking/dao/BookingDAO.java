package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.util.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingDAO extends JpaRepository<Booking, Integer> {

    @Query("select bk from Booking bk join Item i on i.id = bk.item.id where i.owner = ?1 order by bk.start desc")
    List<Booking> findOwnerBookings(Integer ownerId);

    @Query("select bk from Booking bk join Item i on i.id = bk.item.id where i.owner = ?1 and bk.status = ?2 " +
            "order by bk.start desc")
    List<Booking> findOwnerBookingsByStatus(Integer ownerId, Status status);

    @Query("select bk from Booking bk join Item i on i.id = bk.item.id where i.owner = ?1 and bk.end < ?2 " +
            "order by bk.start desc")
    List<Booking> findOwnerBookingsFromPast(Integer ownerId, LocalDateTime dateTime);

    @Query("select bk from Booking bk join Item i on i.id = bk.item.id where i.owner = ?1 and bk.start > ?2 " +
            "order by bk.start desc")
    List<Booking> findOwnerBookingsFromFuture(Integer ownerId, LocalDateTime dateTime);

    @Query("select bk from Booking bk join Item i on i.id = bk.item.id where i.owner = ?1 and bk.start < ?2 and bk.end > ?2 " +
            "order by bk.start desc")
    List<Booking> findOwnerCurrentBookings(Integer ownerId, LocalDateTime dateTime);

    @Query("select bk from Booking bk where bk.booker.id = ?1 order by bk.start desc")
    List<Booking> findBookerBookings(Integer bookerId);

    @Query("select bk from Booking bk where bk.booker.id = ?1 and bk.status = ?2 order by bk.start desc")
    List<Booking> findBookerBookingsByStatus(Integer bookerId, Status status);

    @Query("select bk from Booking bk where bk.booker.id = ?1 and bk.end < ?2 order by bk.start desc")
    List<Booking> findBookerBookingsFromPast(Integer ownerId, LocalDateTime dateTime);

    @Query("select bk from Booking bk where bk.booker.id = ?1 and bk.start > ?2 order by bk.start desc")
    List<Booking> findBookerBookingsFromFuture(Integer ownerId, LocalDateTime dateTime);

    @Query("select bk from Booking bk where bk.booker.id = ?1 and bk.start < ?2 and bk.end > ?2 order by bk.start desc")
    List<Booking> findBookerCurrentBookings(Integer ownerId, LocalDateTime dateTime);

    @Query("select bk from Booking bk where bk.booker.id = ?1 and bk.item.id = ?2")
    List<Booking> findUserBookingsByItem(Integer userId, Integer itemId);

    @Query("select b from Booking b join Item i on i.id = b.item.id where b.item.id = ?1 and b.start > ?2 and " +
            "b.status = 'APPROVED' order by b.start asc")
    List<Booking> getNextBooking(Integer itemId, LocalDateTime dateTime, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id = b.item.id where b.item.id = ?1 and b.end < ?2 and " +
            "b.status = 'APPROVED' order by b.end asc")
    List<Booking> getLastBooking(Integer itemId, LocalDateTime dateTime, Pageable pageable);

}

