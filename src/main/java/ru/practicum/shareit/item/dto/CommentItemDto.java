package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO для представления комментария в объекте вещи
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentItemDto {
    private Integer id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}
