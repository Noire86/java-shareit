package ru.practicum.shareit.booking;

import com.github.javafaker.Faker;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.Status;

import java.time.LocalDateTime;

public abstract class BaseTest {
    protected User user;
    protected User booker;
    protected User dummy;
    protected Item item;
    protected Item item2;
    protected Item unavailable;
    protected Booking booking;
    protected Booking booking2;
    protected Booking futureBooking;
    protected Booking pastBooking;

    private final Faker faker;

    public BaseTest() {
        this.faker = new Faker();
    }

    protected User prepareDetachedUser() {
        User user = new User();
        user.setName(faker.name().name());
        user.setEmail(faker.internet().emailAddress());

        return user;
    }

    protected Item prepareDetachedItem(User owner, boolean available) {
        Item item = new Item();
        item.setOwner(owner.getId());
        item.setName(faker.funnyName().name());
        item.setDescription(faker.beer().name() + faker.beer().name());
        item.setAvailable(available);

        return item;
    }

    protected Booking prepareDetachedBooking(LocalDateTime start, LocalDateTime end, User booker, Item item, Status status) {
        Booking booking = new Booking();
        booking.setStart(start);
        booking.setEnd(end);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(status);

        return booking;
    }
}
