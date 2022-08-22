package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDAO itemDAO;

    @Override
    public ItemDto addItem(Integer ownerId, ItemDto item) {
        return itemDAO.addItem(ownerId, item);
    }

    @Override
    public ItemDto amendItem(Integer ownerId, Integer itemId, ItemDto item) {
        return itemDAO.amendItem(ownerId, itemId, item);
    }

    @Override
    public ItemDto getItem(Integer itemId) {
        return itemDAO.getItem(itemId);
    }

    @Override
    public Collection<ItemDto> getAllItemsByOwner(Integer ownerId) {
        return itemDAO.getAllItemsByOwner(ownerId);
    }

    @Override
    public Collection<ItemDto> search(String searchQuery) {
        return itemDAO.search(searchQuery);
    }
}
