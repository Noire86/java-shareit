package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemDAO {
    Item addItem(Integer ownerId, Item item);

    Item amendItem(Integer itemId, Item item);

    Item getItem(Integer itemId);

    Collection<Item> getAllItemsByOwner(Integer ownerId);

    Collection<Item> search(String searchQuery);
}
