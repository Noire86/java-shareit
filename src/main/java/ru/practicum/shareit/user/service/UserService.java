package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> getUsers();
    UserDto getUser(Integer userId);
    UserDto updateUser(Integer userId, UserDto userDto);
    UserDto createUser(UserDto userDto);

    void deleteUser(Integer userId);

}
