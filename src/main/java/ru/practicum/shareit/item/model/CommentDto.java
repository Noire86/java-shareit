package ru.practicum.shareit.item.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class CommentDto implements Serializable {
    Integer id;
    String text;
    Integer itemId;
    Integer authorId;
    LocalDateTime created;
}
