package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.User.UserBuilder;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Реализация сервиса пользователей
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public Collection<UserDto> getUsers() {
        return userDAO.getAllUsers()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Integer userId) {
        return UserMapper.toUserDto(userDAO.getUserById(userId));
    }

    @Override
    public UserDto updateUser(Integer userId, UserDto userDto) {
        User user = userDAO.getUserById(userId);
        UserBuilder builder = user.toBuilder();

        if (userDto.getName() != null) {
            builder.name(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            if (emailIsValid(userDto.getEmail())) {
                builder.email(userDto.getEmail());
            } else {
                throw new ValidationException(String.format("User with email %s already exists!", user.getEmail()), HttpStatus.CONFLICT);
            }
        }

        return UserMapper.toUserDto(userDAO.update(userId, builder.build()));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);

        if (emailIsValid(user.getEmail())) {
            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
                return UserMapper.toUserDto(userDAO.createUser(user));

            } else {
                throw new ValidationException("User email cannot be empty or null", HttpStatus.BAD_REQUEST);
            }

        } else {
            throw new ValidationException(String.format("User with email %s already exists!", user.getEmail()), HttpStatus.CONFLICT);
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        userDAO.delete(userId);
    }


    private boolean emailIsValid(String email) {
        return userDAO.getAllUsers()
                .stream()
                .map(User::getEmail)
                .noneMatch(str -> str.equals(email));
    }
}
