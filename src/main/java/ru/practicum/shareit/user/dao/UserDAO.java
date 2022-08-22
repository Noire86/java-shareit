package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserDAO {
    UserDto createUser(UserDto userDto);

    UserDto getUserById(Integer id);

    Collection<UserDto> getAllUsers();

    UserDto update(Integer id, UserDto userDto);
}
