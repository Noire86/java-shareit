package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserDAO {
    User createUser(User user);

    User getUserById(Integer userId);

    Collection<User> getAllUsers();

    User update(Integer userId, User user);

    void delete(Integer userId);
}
