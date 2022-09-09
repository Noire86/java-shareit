package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AccessViolationException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserDAO;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Реализация сервиса итемов
 */
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDAO itemDAO;
    private final UserDAO userDAO;

    @Override
    public ItemDto addItem(Integer ownerId, ItemDto item) {
        validate(item);
        User owner = userDAO.getReferenceById(ownerId);
        Item result = ItemMapper.toItem(item, owner.getId());

        return ItemMapper.toItemDto(itemDAO.save(result));
    }

    @Override
    public ItemDto amendItem(Integer ownerId, Integer itemId, ItemDto item) {
        Item result = itemDAO.getReferenceById(itemId);
        result.setId(itemId);

        if (!itemDAO.getReferenceById(itemId).getOwner().equals(ownerId)) {
            throw new AccessViolationException("This user does not have access to this Item!", HttpStatus.FORBIDDEN);
        }

        if (item.getName() != null) {
            result.setName(item.getName());
        }

        if (item.getDescription() != null) {
            result.setDescription(item.getDescription());
        }

        if (item.getAvailable() != null) {
            result.setAvailable(item.getAvailable());
        }

        return ItemMapper.toItemDto(itemDAO.save(result));
    }

    @Override
    public ItemDto getItem(Integer itemId) {
        return ItemMapper.toItemDto(itemDAO.getReferenceById(itemId));
    }

    @Override
    public Collection<ItemDto> getAllItemsByOwner(Integer ownerId) {
        return itemDAO.findByOwnerEquals(ownerId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> search(String searchQuery) {
        return itemDAO.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(searchQuery, searchQuery)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private boolean validate(ItemDto item) {
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
