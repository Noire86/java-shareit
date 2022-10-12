package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ItemDAOTest extends BaseTest {

    @Autowired
    private ItemDAO itemDAO;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void prepareEntities() {
        user = em.persistFlushFind(prepareDetachedUser());
        user2 = em.persistFlushFind(prepareDetachedUser());

        item = em.persistFlushFind(prepareDetachedItem(user, true));
        item1 = em.persistFlushFind(prepareDetachedItem(user, true));
        item2 = em.persistFlushFind(prepareDetachedItem(user2, true));
    }

    @Test
    void findByOwnerEquals() {
        List<Item> result = itemDAO.findByOwnerEquals(user.getId(), Pageable.unpaged());

        assertEquals(result.size(), 2);
        assertTrue(result.contains(item));
        assertTrue(result.contains(item1));
        assertFalse(result.contains(item2));
    }

    @Test
    void findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue() {
        item.setDescription("test");
        item1.setDescription("test test");
        item2.setDescription("test test test");
        item2.setAvailable(false);

        List<Item> result = itemDAO.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(
                "test", "test", Pageable.unpaged());

        assertEquals(2, result.size());
        assertTrue(result.contains(item));
        assertTrue(result.contains(item1));
        assertFalse(result.contains(item2));
    }

    @Test
    void findAllByRequest() {
        item.setRequest(1);
        item1.setRequest(1);

        List<Item> result = itemDAO.findAllByRequest(1);
        assertEquals(2, result.size());
        assertTrue(result.contains(item));
        assertTrue(result.contains(item1));
        assertFalse(result.contains(item2));
    }
}