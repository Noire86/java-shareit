package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.booking.dao.BookingDAO;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentDAO;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;

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
        user2 = prepareDetachedUser();

        item = prepareDetachedItem(user, true);
        item2 = prepareDetachedItem(user2, true);
        unavailable = prepareDetachedItem(user, false);
    }

    @Test
    void addItem() {
        user.setId(1);
        ItemDto itemDto = ItemDto.builder()
                .name(faker.funnyName().name())
                .description(faker.letterify("###########"))
                .available(true)
                .build();

        Item persisted = ItemMapper.toItem(itemDto, user.getId());
        persisted.setId(user.getId());

        when(userDAO.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(itemDAO.save(any(Item.class)))
                .thenReturn(persisted);

        ItemDto result = service.addItem(user.getId(), itemDto);

        assertNotNull(result);
        assertEquals(result.getId(), 1);
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
    }

    @Test
    void getItem() {
    }

    @Test
    void getAllItemsByOwner() {
    }

    @Test
    void search() {
    }

    @Test
    void addNewComment() {
    }
}