package ru.practicum.shareit.user.dao;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.StorageException;
import ru.practicum.shareit.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Реализация репозитория пользователей с хранением в памяти
 */
@Repository
public class MemoryUserDAOImpl implements UserDAO {

    private final Map<Integer, User> users = new HashMap<>();
    private int counter = 0;

    @Override
    public User createUser(User user) {
        User result = user
                .toBuilder()
                .id(++counter)
                .build();

        users.put(result.getId(), result);
        return result;
    }

    @Override
    public User getUserById(Integer userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new StorageException(String.format("User with ID: %d was not found", userId), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User update(Integer userId, User user) {
        if (users.containsKey(userId)) {
            users.put(userId, user);
            return user;

        } else {
            throw new StorageException(String.format("User with ID: %d was not found", userId), HttpStatus.NOT_FOUND);
        }

    }

    @Override
    public void delete(Integer userId) {
        users.remove(userId);
    }
}
