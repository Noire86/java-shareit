package ru.practicum.shareit.requests.mapper;

import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestExtendedDto;
import ru.practicum.shareit.requests.dto.ItemResponseDto;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest request) {
        return ItemRequestDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requestorId(request.getRequestor().getId())
                .created(request.getCreated())
                .build();
    }

    public static ItemRequestExtendedDto toItemRequestExtendedDto(ItemRequest request, List<Item> items) {
        List<ItemResponseDto> responses = items.stream()
                .map(ItemMapper::toItemResponseDto)
                .collect(Collectors.toList());

        return ItemRequestExtendedDto.builder()
                .id(request.getId())
                .description(request.getDescription())
                .requestorId(request.getRequestor().getId())
                .created(request.getCreated())
                .items(responses)
                .build();
    }
}
