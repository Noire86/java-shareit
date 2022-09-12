package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingItemDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ItemExtendedDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer request;
    private BookingItemDto nextBooking;
    private BookingItemDto lastBooking;
    private List<CommentItemDto> comments;
}
