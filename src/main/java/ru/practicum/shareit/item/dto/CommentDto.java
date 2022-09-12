package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Базовый DTO комментария
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentDto implements Serializable {
    private Integer id;
    private String text;
    private Item item;
    private String authorName;
    private LocalDateTime created;
}
