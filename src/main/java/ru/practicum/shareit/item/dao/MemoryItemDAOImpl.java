package ru.practicum.shareit.item.dao;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.StorageException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Реализация ДАО репозитория итемов с хранением в памяти
 */
@Component
public class MemoryItemDAOImpl implements ItemDAO {

    private final Map<Integer, Item> items = new HashMap<>();
    private int counter = 0;

    @Override
    public Item addItem(Integer ownerId, Item item) {

        Item result = item.toBuilder()
                .id(++counter)
                .build();

        items.put(result.getId(), result);
        return result;
    }

    @Override
    public Item amendItem(Integer itemId, Item item) {
        if (items.containsKey(itemId)) {
            items.put(itemId, item);
            return item;
        } else {
            throw new StorageException(String.format("There are no any items with ID %d!", itemId), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Item getItem(Integer itemId) {
        if (items.containsKey(itemId)) {
            return items.get(itemId);
        } else {
            throw new StorageException(String.format("There are no any items with ID %d!", itemId), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<Item> getAllItemsByOwner(Integer ownerId) {
        return items.values()
                .stream()
                .filter(i -> i.getOwner().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> search(String searchQuery) {
        if (searchQuery.isEmpty()) return new ArrayList<>();

        return items.values()
                .stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getName().toLowerCase().contains(searchQuery.toLowerCase()) ||
                        i.getDescription().toLowerCase().contains(searchQuery.toLowerCase()))
                .collect(Collectors.toList());
    }
}
