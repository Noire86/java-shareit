package ru.practicum.shareit.requests.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemRequestDAOTest extends BaseTest {

    @Autowired
    private ItemRequestDAO itemRequestDAO;

    @Autowired
    private TestEntityManager em;


    @BeforeEach
    void prepareEntities() {
        user = em.persistFlushFind(prepareDetachedUser());
        user2 = em.persistFlushFind(prepareDetachedUser());


        itemRequest1 = em.persistFlushFind(prepareDetachedItemRequest(user));
        itemRequest2 = em.persistFlushFind(prepareDetachedItemRequest(user));
        itemRequest3 = em.persistFlushFind(prepareDetachedItemRequest(user2));
    }

    @Test
    void getItemRequestsByRequestor() {
        List<ItemRequest> result = itemRequestDAO.getItemRequestsByRequestor(user.getId());

        assertEquals(2, result.size());
        assertTrue(result.contains(itemRequest1));
        assertTrue(result.contains(itemRequest2));
        assertFalse(result.contains(itemRequest3));
    }

    @Test
    void getAllItemRequests() {
        List<ItemRequest> result = itemRequestDAO.getAllItemRequests(Pageable.unpaged());

        assertEquals(3, result.size());
        assertTrue(result.contains(itemRequest1));
        assertTrue(result.contains(itemRequest2));
        assertTrue(result.contains(itemRequest3));
    }
}