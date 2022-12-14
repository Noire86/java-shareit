package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingDAO;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.AccessViolationException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentDAO;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.util.PaginationUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса итемов
 */
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDAO itemDAO;
    private final UserDAO userDAO;
    private final BookingDAO bookingDAO;
    private final CommentDAO commentDAO;

    @Override
    public ItemDto addItem(Integer ownerId, ItemDto item) {
        User owner = userDAO.findById(ownerId).orElseThrow(EntityNotFoundException::new);
        Item result = ItemMapper.toItem(item, owner.getId());

        return ItemMapper.toItemDto(itemDAO.save(result));
    }

    @Override
    public ItemDto amendItem(Integer ownerId, Integer itemId, ItemDto item) {
        Item result = itemDAO.findById(itemId).orElseThrow(EntityNotFoundException::new);
        result.setId(itemId);

        if (!itemDAO.findById(itemId).orElseThrow(EntityNotFoundException::new).getOwner().equals(ownerId)) {
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
    public ItemExtendedDto getItem(Integer userId, Integer itemId) {
        Item item = itemDAO.findById(itemId).orElseThrow(EntityNotFoundException::new);
        boolean isOwner = item.getOwner().equals(userId);

        ItemExtendedDto result = ItemMapper.toItemExtendedDto(item);
        List<Booking> nextBooking = bookingDAO.getNextBooking(itemId);
        List<Booking> lastBooking = bookingDAO.getLastBooking(itemId);

        List<CommentItemDto> comments = commentDAO.getCommentsByItemId(itemId)
                .stream()
                .map(CommentMapper::toCommentItemDto)
                .collect(Collectors.toList());

        result = result.toBuilder()
                .comments(comments)
                .lastBooking(isOwner && lastBooking.size() > 0 ? BookingMapper.toBookingItemInfoDto(lastBooking.get(0)) : null)
                .nextBooking(isOwner && nextBooking.size() > 0 ? BookingMapper.toBookingItemInfoDto(nextBooking.get(0)) : null)
                .build();

        return result;

    }

    @Override
    public Collection<ItemExtendedDto> getAllItemsByOwner(Integer ownerId, Integer from, Integer size) {
        Pageable pageable = PaginationUtils.handlePaginationParams(from, size);

        return itemDAO.findByOwnerEqualsOrderByIdAsc(ownerId, pageable)
                .stream()
                .map(i -> getItem(ownerId, i.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> search(String searchQuery, Integer from, Integer size) {
        Pageable pageable = PaginationUtils.handlePaginationParams(from, size);

        return itemDAO.findAllByNameOrDescriptionContainingIgnoreCaseAndAvailableIsTrue(searchQuery, searchQuery, pageable)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addNewComment(Integer commenterId, Integer itemId, CommentCreationDto commentCreationDto) {
        Comment comment = CommentMapper.toComment(commentCreationDto);
        User commenter = userDAO.findById(commenterId).orElseThrow(EntityNotFoundException::new);
        Item item = itemDAO.findById(itemId).orElseThrow(EntityNotFoundException::new);

        if (bookingDAO.findValidUserBookingsByItem(commenterId, itemId).size() < 1) {
            throw new AccessViolationException("This user cannot leave a comment, because he has never booked this item",
                    HttpStatus.BAD_REQUEST);
        }

        if (!item.getAvailable()) {
            throw new ValidationException("You cannot leave comment for unavailable item booking",
                    HttpStatus.BAD_REQUEST);
        }

        if (commentCreationDto.getText().isEmpty())
            throw new ValidationException("Empty comment text!", HttpStatus.BAD_REQUEST);

        comment.setAuthor(commenter);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toCommentDto(commentDAO.save(comment));

    }
}
