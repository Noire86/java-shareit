package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

/**
 * Интерфейс для сервиса итемов
 */
public interface ItemService {

    ItemDto addItem(Integer ownerId, ItemDto item);

    ItemDto amendItem(Integer ownerId, Integer itemId, ItemDto item);

    ItemDto getItem(Integer itemId);

    Collection<ItemDto> getAllItemsByOwner(Integer ownerId);

    Collection<ItemDto> search(String searchQuery);
}
