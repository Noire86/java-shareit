package ru.practicum.shareit.item.dao;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AccessViolationException;
import ru.practicum.shareit.exception.StorageException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MemoryItemDAOImpl implements ItemDAO {

    private final Map<Integer, Item> items = new HashMap<>();
    private int counter = 0;

    @Override
    public ItemDto addItem(Integer ownerId, ItemDto item) {

        ItemDto result = item.toBuilder()
                .id(++counter)
                .build();

        items.put(result.getId(), ItemMapper.toItem(result, ownerId));
        return result;
    }

    @Override
    public ItemDto amendItem(Integer ownerId, Integer itemId, ItemDto item) {
        if (items.containsKey(itemId)) {
            if (items.get(itemId).getOwner().equals(ownerId)) {
                items.put(itemId, ItemMapper.toItem(item, ownerId));
                return item;
            } else {
                throw new AccessViolationException("This user does not have access to this Item!", HttpStatus.FORBIDDEN);
            }
        } else {
            throw new StorageException(String.format("There are no any items with ID %d!", itemId), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ItemDto getItem(Integer itemId) {
        if (items.containsKey(itemId)) {
            return ItemMapper.toItemDto(items.get(itemId));
        } else {
            throw new StorageException(String.format("There are no any items with ID %d!", itemId), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public Collection<ItemDto> getAllItemsByOwner(Integer ownerId) {
        return items.values()
                .stream()
                .filter(i -> i.getOwner().equals(ownerId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> search(String searchQuery) {
        return items.values()
                .stream()
                .filter(i -> i.getName().contains(searchQuery) || i.getDescription().contains(searchQuery))
                .filter(Item::isAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
