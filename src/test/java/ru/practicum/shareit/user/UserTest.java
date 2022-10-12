package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.BaseTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserTest extends BaseTest {

    @BeforeEach
    void setupEntities() {
        user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@test");

        user2 = new User();
        user2.setId(1);
        user2.setName("test");
        user2.setEmail("test@test");
    }

    @Test
    void testEquals() {
        assertEquals(user, user2);
    }

    @Test
    void testHashCode() {
        assertEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    void testToString() {
        assertEquals(user.toString(), user2.toString());
    }
}