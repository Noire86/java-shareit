package ru.practicum.shareit.booking;

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
public class Booking {

    Integer id;
    LocalDateTime start;
    LocalDateTime end;
    Integer itemId;
    Integer bookerId;
    Status status;
}
