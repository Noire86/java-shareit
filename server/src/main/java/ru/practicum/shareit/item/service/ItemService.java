package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;

import java.util.Collection;

/**
 * Интерфейс для сервиса итемов
 */
public interface ItemService {

    ItemDto addItem(Integer ownerId, ItemDto item);

    ItemDto amendItem(Integer ownerId, Integer itemId, ItemDto item);

    ItemExtendedDto getItem(Integer userId, Integer itemId);

    Collection<ItemExtendedDto> getAllItemsByOwner(Integer ownerId, Integer from, Integer size);

    Collection<ItemDto> search(String searchQuery, Integer from, Integer size);

    CommentDto addNewComment(Integer commenterId, Integer itemId, CommentCreationDto commentCreationDto);
}
