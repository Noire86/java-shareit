package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO для создания комментария
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentCreationDto implements Serializable {
    private String text;
}