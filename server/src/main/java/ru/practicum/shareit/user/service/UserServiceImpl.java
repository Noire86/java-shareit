package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import javax.persistence.EntityNotFoundException;
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
        return userDAO.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Integer userId) {
        return UserMapper.toUserDto(
                userDAO.findById(userId)
                        .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public UserDto updateUser(Integer userId, UserDto userDto) {
        User user = userDAO.findById(userId)
                .orElseThrow(EntityNotFoundException::new);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            user.setEmail(validateEmail(userDto.getEmail()));
        }

        return UserMapper.toUserDto(userDAO.save(user));
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userDAO.save(user));
    }

    @Override
    public void deleteUser(Integer userId) {
        userDAO.deleteById(userId);
    }

    private String validateEmail(String email) {
        if (userDAO.findAll()
                .stream()
                .map(User::getEmail)
                .anyMatch(str -> str.equals(email))) {
            throw new ValidationException(String.format("User with email %s already exists!", email),
                    HttpStatus.CONFLICT);
        }

        return email;
    }
}
