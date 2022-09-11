package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.model.CommentItemDto;

import java.util.List;

@Value
@Builder(toBuilder = true)
public class ItemExtendedDto {
    Integer id;
    String name;
    String description;
    Boolean available;
    Integer request;
    BookingItemDto nextBooking;
    BookingItemDto lastBooking;
    List<CommentItemDto> comments;
}
