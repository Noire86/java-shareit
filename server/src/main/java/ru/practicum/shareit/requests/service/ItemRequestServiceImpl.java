package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.requests.dao.ItemRequestDAO;
import ru.practicum.shareit.requests.dto.ItemRequestCreationDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestExtendedDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.util.PaginationUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestDAO itemRequestDAO;
    private final UserDAO userDAO;
    private final ItemDAO itemDAO;

    @Override
    public ItemRequestDto createRequest(Integer userId, ItemRequestCreationDto itemRequestCreationDto) {
        User requestor = userDAO.findById(userId).orElseThrow(EntityNotFoundException::new);
        ItemRequest result = new ItemRequest();
        result.setDescription(itemRequestCreationDto.getDescription());
        result.setRequestor(requestor);
        result.setCreated(LocalDateTime.now());

        return ItemRequestMapper.toItemRequestDto(itemRequestDAO.save(result));
    }

    @Override
    public List<ItemRequestExtendedDto> getPersonalItemRequests(Integer userId) {
        User user = userDAO.findById(userId).orElseThrow(EntityNotFoundException::new);
        List<ItemRequest> userRequests = itemRequestDAO.getItemRequestsByRequestor(user.getId());

        return userRequests.stream()
                .map(ir -> ItemRequestMapper.toItemRequestExtendedDto(ir, itemDAO.findAllByRequest(ir.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestExtendedDto> getAllItemRequestsPaged(Integer userId, Integer from, Integer size) {
        Pageable pageable = PaginationUtils.handlePaginationParams(from, size);
        List<ItemRequest> allRequests = itemRequestDAO.getAllItemRequests(pageable);

        return allRequests.stream()
                .filter(ir -> !ir.getRequestor().getId().equals(userId))
                .map(ir -> ItemRequestMapper.toItemRequestExtendedDto(ir, itemDAO.findAllByRequest(ir.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestExtendedDto getItemRequestById(Integer userId, Integer requestId) {
        userDAO.findById(userId).orElseThrow(EntityNotFoundException::new);
        ItemRequest result = itemRequestDAO.findById(requestId).orElseThrow(EntityNotFoundException::new);
        return ItemRequestMapper.toItemRequestExtendedDto(result, itemDAO.findAllByRequest(result.getId()));
    }
}
