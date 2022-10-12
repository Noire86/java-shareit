package ru.practicum.shareit;

import com.github.javafaker.Faker;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.util.Status;

import java.time.LocalDateTime;

public abstract class BaseTest {
    protected User user;
    protected User user2;
    protected User user3;
    protected User booker;
    protected User dummy;
    protected Item item;
    protected Item item1;
    protected Item item2;
    protected Item unavailable;
    protected Booking booking;
    protected Booking booking2;
    protected Booking futureBooking;
    protected Booking pastBooking;
    protected Comment comment1;
    protected Comment comment2;
    protected ItemRequest itemRequest1;
    protected ItemRequest itemRequest2;
    protected ItemRequest itemRequest3;

    public final Faker faker;

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
        item.setRequest(faker.number().randomDigit());

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

    protected Comment prepareDetachedComment(Item item, User author) {
        Comment comment = new Comment();
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setText(faker.book().title());
        comment.setCreated(LocalDateTime.now());

        return comment;
    }

    protected ItemRequest prepareDetachedItemRequest(User requestor) {
        ItemRequest ir = new ItemRequest();
        ir.setRequestor(requestor);
        ir.setCreated(LocalDateTime.now());
        ir.setDescription(faker.medical().medicineName());

        return ir;
    }
}
