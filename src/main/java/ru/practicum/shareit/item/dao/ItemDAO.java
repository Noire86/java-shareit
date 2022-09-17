package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Интерфейс для репозитория Item-ов
 */
public interface ItemDAO extends JpaRepository<Item, Integer> {

    List<Item> findByOwnerEquals(Integer ownerId);

    List<Item> findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(String name, String desc);
}
