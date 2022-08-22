package ru.practicum.shareit.user.dao;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.StorageException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MemoryUserDaoImpl implements UserDAO{

    private final Map<Integer, User> users = new HashMap<>();
    private int counter = 0;

    @Override
    public UserDto createUser(UserDto userDto) {
        UserDto result = userDto.toBuilder()
                .id(++counter)
                .build();

        users.put(result.getId(), UserMapper.toUser(result));
        return result;
    }

    @Override
    public UserDto getUserById(Integer id) {
        if (users.containsKey(id)) {
            return UserMapper.toUserDto(users.get(id));
        } else {
            throw new StorageException(String.format("User with ID: %d was not found", id), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<UserDto> getAllUsers() {
        return users.values()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto update(Integer id, UserDto userDto) {
        if (users.containsKey(id)) {
            users.put(id, UserMapper.toUser(userDto));
            return userDto;
        } else {
            throw new StorageException(String.format("User with ID: %d was not found", id), HttpStatus.NOT_FOUND);
        }

    }
}
