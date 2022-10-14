package java.ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.item.model.Item;

class ItemTest extends BaseTest {

    @BeforeEach
    void prepareEntities() {
        item = new Item();
        item.setName("test");
        item.setDescription("test");
        item.setId(1);
        item.setAvailable(true);
        item.setRequest(1);
        item.setOwner(1);

        item2 = new Item();
        item2.setName("test");
        item2.setDescription("test");
        item2.setId(1);
        item2.setAvailable(true);
        item2.setRequest(1);
        item2.setOwner(1);
    }

    @Test
    void testEquals() {
        assertEquals(item, item2);
    }

    @Test
    void testHashCode() {
        assertEquals(item.hashCode(), item2.hashCode());
    }
}