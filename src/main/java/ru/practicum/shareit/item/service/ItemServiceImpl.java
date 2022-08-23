package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessViolationException;
import ru.practicum.shareit.exception.StorageException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDAO itemDAO;
    private final UserDAO userDAO;

    @Override
    public ItemDto addItem(Integer ownerId, ItemDto item) {
        Item result = ItemMapper.toItem(item, ownerId);

        if (itemIsValid(item) && userDAO.getUserById(ownerId) == null) {
            throw new StorageException("Cannot add new item for non-existent user!", HttpStatus.NOT_FOUND);
        }

        return ItemMapper.toItemDto(itemDAO.addItem(ownerId, result));
    }

    @Override
    public ItemDto amendItem(Integer ownerId, Integer itemId, ItemDto item) {
        Item.ItemBuilder builder = itemDAO.getItem(itemId).toBuilder();

        if (!itemDAO.getItem(itemId).getOwner().equals(ownerId)) {
            throw new AccessViolationException("This user does not have access to this Item!", HttpStatus.FORBIDDEN);
        }

        if (item.getName() != null) {
            builder.name(item.getName());
        }

        if (item.getDescription() != null) {
            builder.description(item.getDescription());
        }

        if (item.getAvailable() != null) {
            builder.available(item.getAvailable());
        }

        return ItemMapper.toItemDto(itemDAO.amendItem(itemId, builder.build()));
    }

    @Override
    public ItemDto getItem(Integer itemId) {
        return ItemMapper.toItemDto(itemDAO.getItem(itemId));
    }

    @Override
    public Collection<ItemDto> getAllItemsByOwner(Integer ownerId) {
        return itemDAO.getAllItemsByOwner(ownerId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> search(String searchQuery) {
        return itemDAO.search(searchQuery)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private boolean itemIsValid(ItemDto item) {
        if (item.getName() == null || item.getName().isEmpty()) {
            throw new ValidationException("Item name cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new ValidationException("Item description cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if (item.getAvailable() == null) {
            throw new ValidationException("Item availability cannot be empty", HttpStatus.BAD_REQUEST);
        }

        return true;
    }
}
