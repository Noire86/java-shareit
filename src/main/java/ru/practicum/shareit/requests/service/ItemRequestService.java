package ru.practicum.shareit.requests.service;


import ru.practicum.shareit.requests.dto.ItemRequestCreationDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestExtendedDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createRequest(Integer userId, ItemRequestCreationDto itemRequestCreationDto);
    List<ItemRequestExtendedDto> getPersonalItemRequests(Integer userId);
    List<ItemRequestExtendedDto> getAllItemRequestsPaged(Integer userId, Integer from, Integer size);
    ItemRequestExtendedDto getItemRequestById(Integer userId, Integer requestId);
}
