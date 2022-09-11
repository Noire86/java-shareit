package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class CommentItemDto {
    Integer id;
    String text;
    String authorName;
    LocalDateTime created;
}
