package java.ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest extends BaseTest {

    @MockBean
    private UserDAO userDAO;

    @Autowired
    private UserService service;

    @BeforeEach
    void setupEntities() {
        user = prepareDetachedUser();
        user2 = prepareDetachedUser();
        user3 = prepareDetachedUser();
    }

    @BeforeEach
    void setupMocks() {
        when(userDAO.findAll())
                .thenReturn(List.of(user, user2, user3));
    }

    @Test
    void getUsers() {
        Collection<UserDto> users = service.getUsers();

        assertEquals(3, users.size());
        assertTrue(users.contains(UserMapper.toUserDto(user)));
        assertTrue(users.contains(UserMapper.toUserDto(user2)));
        assertTrue(users.contains(UserMapper.toUserDto(user3)));

    }

    @Test
    void getUser() {
        when(userDAO.findById(anyInt()))
                .thenReturn(Optional.of(user));

        UserDto userDto = service.getUser(1);
        assertEquals(UserMapper.toUserDto(user), userDto);
    }

    @Test
    void updateValidUser() {
        UserDto updated = UserDto.builder()
                .name("Updated User")
                .email("NewEmail@goog.com")
                .build();

        when(userDAO.findById(anyInt()))
                .thenReturn(Optional.of(user));

        when(userDAO.save(user))
                .thenReturn(user);

        UserDto result = service.updateUser(1, updated);

        assertEquals(updated.getName(), result.getName());
    }

    @Test
    void updateInvalidEmailUser() {
        user.setId(1);
        user.setName("Updated User");
        user.setEmail(user3.getEmail());

        UserDto updated = UserMapper.toUserDto(user);

        when(userDAO.findById(anyInt()))
                .thenReturn(Optional.of(user));

        when(userDAO.save(user))
                .thenReturn(user);

        ValidationException ex = assertThrows(ValidationException.class, () -> service.updateUser(1, updated));
        assertEquals("User with email " + user3.getEmail() + " already exists!", ex.getMessage());
    }

    @Test
    void createValidUser() {
        when(userDAO.save(any()))
                .thenReturn(user);

        UserDto origin = UserMapper.toUserDto(user);
        UserDto result = service.createUser(origin);

        assertEquals(origin, result);
    }

    @Test
    void createBadEmailUser() {
        user.setEmail("");
        when(userDAO.save(any()))
                .thenReturn(user);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.createUser(UserMapper.toUserDto(user)));
        assertEquals("User email cannot be empty or null", ex.getMessage());
    }

    @Test
    void deleteUser() {
        service.deleteUser(1);
    }
}