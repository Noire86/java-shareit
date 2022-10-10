package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.booking.dao.BookingDAO;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.AccessViolationException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentDAO;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.util.Status;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemServiceImplTest extends BaseTest {

    @MockBean
    private ItemDAO itemDAO;

    @MockBean
    private UserDAO userDAO;

    @MockBean
    private BookingDAO bookingDAO;

    @MockBean
    private CommentDAO commentDAO;

    @Autowired
    private ItemService service;

    @BeforeEach
    void prepareEntities() {
        user = prepareDetachedUser();
        user.setId(1);
        user2 = prepareDetachedUser();
        user2.setId(2);
        booker = prepareDetachedUser();
        booker.setId(3);

        item = prepareDetachedItem(user, true);
        item1 = prepareDetachedItem(user2, true);
        item2 = prepareDetachedItem(user2, true);
        unavailable = prepareDetachedItem(user, false);

        booking = prepareDetachedBooking(
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(5),
                booker,
                item,
                Status.WAITING);

        futureBooking = prepareDetachedBooking(
                LocalDateTime.now().plusDays(4),
                LocalDateTime.now().plusDays(5),
                booker,
                item,
                Status.APPROVED);

        pastBooking = prepareDetachedBooking(
                LocalDateTime.now().minusDays(5),
                LocalDateTime.now().minusDays(3),
                booker,
                item,
                Status.APPROVED);
    }

    @Test
    void addItem() {
        user.setId(1);
        ItemDto itemDto = ItemDto.builder()
                .name(faker.funnyName().name())
                .description(faker.letterify("###########"))
                .available(true)
                .build();

        when(userDAO.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(itemDAO.save(any(Item.class)))
                .then(AdditionalAnswers.returnsFirstArg());

        ItemDto result = service.addItem(user.getId(), itemDto);

        assertNotNull(result);
        assertEquals(result.getName(), itemDto.getName());
        assertEquals(result.getDescription(), itemDto.getDescription());
        assertEquals(result.getAvailable(), itemDto.getAvailable());
    }

    @Test
    void addItemInvalidName() {
        ItemDto itemDto = ItemDto.builder()
                .name("")
                .description(faker.letterify("###########"))
                .available(true)
                .build();

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addItem(user.getId(), itemDto));
        assertEquals(ex.getMessage(), "Item name cannot be empty");
    }

    @Test
    void addItemInvalidDesc() {
        ItemDto itemDto = ItemDto.builder()
                .name("Test")
                .description(faker.letterify(""))
                .available(true)
                .build();

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addItem(user.getId(), itemDto));
        assertEquals(ex.getMessage(), "Item description cannot be empty");
    }

    @Test
    void addItemNotAvailable() {
        ItemDto itemDto = ItemDto.builder()
                .name("Test")
                .description(faker.letterify("#####"))
                .available(null)
                .build();

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addItem(user.getId(), itemDto));
        assertEquals(ex.getMessage(), "Item availability cannot be empty");
    }


    @Test
    void amendItem() {
        ItemDto itemDto = ItemDto.builder()
                .name("Test")
                .description(faker.letterify("#####"))
                .available(true)
                .build();

        when(itemDAO.findById(anyInt()))
                .thenReturn(Optional.of(item));

        when(itemDAO.save(any()))
                .then(AdditionalAnswers.returnsFirstArg());

        ItemDto result = service.amendItem(1, 1, itemDto);

        assertNotNull(result);
        assertEquals(result.getId(), 1);
        assertEquals(result.getName(), itemDto.getName());
        assertEquals(result.getDescription(), itemDto.getDescription());
        assertEquals(result.getAvailable(), itemDto.getAvailable());
    }

    @Test
    void amendItemBynInvalidUser() {
        ItemDto itemDto = ItemDto.builder()
                .name("Test")
                .description(faker.letterify("#####"))
                .available(true)
                .build();

        when(itemDAO.findById(1))
                .thenReturn(Optional.of(item));

        AccessViolationException ex = assertThrows(AccessViolationException.class,
                () -> service.amendItem(2, 1, itemDto));

        assertEquals(ex.getMessage(), "This user does not have access to this Item!");
    }

    @Test
    void getItem() {
        Comment comment1 = prepareDetachedComment(item, user2);
        Comment comment2 = prepareDetachedComment(item, user2);

        ItemExtendedDto dto = ItemExtendedDto.builder()
                .comments(List.of(CommentMapper.toCommentItemDto(comment1), CommentMapper.toCommentItemDto(comment2)))
                .name(item.getName())
                .description(item.getDescription())
                .available(true)
                .request(item.getRequest())
                .lastBooking(BookingMapper.toBookingItemInfoDto(pastBooking))
                .nextBooking(BookingMapper.toBookingItemInfoDto(futureBooking))
                .build();


        when(itemDAO.findById(1))
                .thenReturn(Optional.of(item));

        when(bookingDAO.getLastBooking(1))
                .thenReturn(List.of(pastBooking));

        when(bookingDAO.getNextBooking(1))
                .thenReturn(List.of(futureBooking));

        when(commentDAO.getCommentsByItemId(1))
                .thenReturn(List.of(comment1, comment2));

        ItemExtendedDto result = service.getItem(1, 1);

        assertEquals(result, dto);
    }

    @Test
    void getAllItemsByOwner() {
        item.setId(1);
        item1.setId(2);

        when(itemDAO.findById(1))
                .thenReturn(Optional.of(item));

        when(itemDAO.findById(2))
                .thenReturn(Optional.of(item1));

        when(itemDAO.findByOwnerEquals(anyInt(), any()))
                .thenReturn(List.of(item, item1));

        when(bookingDAO.getLastBooking(anyInt()))
                .thenReturn(Collections.emptyList());

        when(bookingDAO.getNextBooking(anyInt()))
                .thenReturn(Collections.emptyList());

        when(commentDAO.getCommentsByItemId(1))
                .thenReturn(Collections.emptyList());

        Collection<ItemExtendedDto> result = service.getAllItemsByOwner(user.getId(), 0, 10);
        assertEquals(result.size(), 2);
        assertTrue(result.contains(ItemMapper.toItemExtendedDto(item)));
        assertTrue(result.contains(ItemMapper.toItemExtendedDto(item1)));
    }

    @Test
    void search() {
        Collection<ItemDto> emptyResult = service.search("", 0, 2);
        assertEquals(emptyResult.size(), 0);

        item.setName("test");

        when(itemDAO.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                eq("test"), eq("test"), any()))
                .thenReturn(List.of(item));

        Collection<ItemDto> result = service.search("test", 0, 2);
        assertEquals(result.size(), 1);
        assertEquals(result, List.of(ItemMapper.toItemDto(item)));
    }

    @Test
    void addNewValidComment() {
        CommentCreationDto dto = CommentCreationDto.builder()
                .text("test")
                .build();

        user.setId(1);
        when(userDAO.findById(1))
                .thenReturn(Optional.of(user));

        when(itemDAO.findById(1))
                .thenReturn(Optional.of(item));

        when(bookingDAO.findValidUserBookingsByItem(1, 1))
                .thenReturn(List.of(booking));

        when(commentDAO.save(any()))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        CommentDto result = service.addNewComment(1, 1, dto);

        assertEquals(dto.getText(), result.getText());
        assertEquals(result.getItem(), item);
        assertEquals(result.getAuthorName(), user.getName());
    }

    @Test
    void addNewCommentWithInvalidCommenter() {
        CommentCreationDto dto = CommentCreationDto.builder()
                .text("test")
                .build();

        user.setId(1);
        when(userDAO.findById(1))
                .thenReturn(Optional.of(user));

        when(itemDAO.findById(1))
                .thenReturn(Optional.of(item));

        when(bookingDAO.findValidUserBookingsByItem(1, 1))
                .thenReturn(Collections.emptyList());

        AccessViolationException ex = assertThrows(AccessViolationException.class,
                () -> service.addNewComment(1, 1, dto));
        assertEquals(ex.getMessage(), "This user cannot leave a comment, because he has never booked this item");
    }

    @Test
    void addNewCommentWithUnavailableItem() {
        CommentCreationDto dto = CommentCreationDto.builder()
                .text("test")
                .build();

        user.setId(1);
        item.setAvailable(false);

        when(userDAO.findById(1))
                .thenReturn(Optional.of(user));

        when(itemDAO.findById(1))
                .thenReturn(Optional.of(item));

        when(bookingDAO.findValidUserBookingsByItem(1, 1))
                .thenReturn(List.of(booking));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addNewComment(1, 1, dto));
        assertEquals(ex.getMessage(), "You cannot leave comment for unavailable item booking");
    }

    @Test
    void addNewCommentWithEmptyText() {
        CommentCreationDto dto = CommentCreationDto.builder()
                .text("")
                .build();

        user.setId(1);
        when(userDAO.findById(1))
                .thenReturn(Optional.of(user));

        when(itemDAO.findById(1))
                .thenReturn(Optional.of(item));

        when(bookingDAO.findValidUserBookingsByItem(1, 1))
                .thenReturn(List.of(booking));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addNewComment(1, 1, dto));
        assertEquals(ex.getMessage(), "Empty comment text!");
    }
}