package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

/**
 * Маппер-класс для итемов
 */
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest())
                .build();
    }

    public static Item toItem(ItemDto itemDto, Integer ownerId) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setRequest(itemDto.getRequest());
        item.setOwner(ownerId);

        return item;
    }
}
