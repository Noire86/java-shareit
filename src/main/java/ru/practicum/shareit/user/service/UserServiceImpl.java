package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public Collection<UserDto> getUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public UserDto getUser(Integer userId) {
        return userDAO.getUserById(userId);
    }

    @Override
    public UserDto updateUser(Integer userId, UserDto userDto) {
        return userDAO.update(userId, userDto);
    }

    @Override
    public UserDto createUser(UserDto user) {
        return userDAO.createUser(user);
    }
}
