package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.stream.Collector;

public interface ItemDAO {
    ItemDto addItem(Integer ownerId, ItemDto item);
    ItemDto amendItem(Integer ownerId, Integer itemId, ItemDto item);
    ItemDto getItem(Integer itemId);
    Collection<ItemDto> getAllItemsByOwner(Integer ownerId);
    Collection<ItemDto> search(String searchQuery);
}
