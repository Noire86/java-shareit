package ru.practicum.shareit.booking.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.Status;

import javax.persistence.*;
import java.time.LocalDateTime;

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
}
