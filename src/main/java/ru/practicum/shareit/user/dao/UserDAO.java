package ru.practicum.shareit.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

/**
 * Интерфейс репозитория пользователей
 */
public interface UserDAO extends JpaRepository<User, Integer> {

}
