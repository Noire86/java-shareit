package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * // Модель бронирования
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "bookings", schema = "public")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "start_date", nullable = false)
    LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    Item item;

    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    User booker;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Status status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) && Objects.equals(start, booking.start) && Objects.equals(end, booking.end) && Objects.equals(item, booking.item) && Objects.equals(booker, booking.booker) && status == booking.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start, end, item, booker, status);
    }
}
