package ru.practicum.shareit.item.model;

import lombok.*;

import java.io.Serializable;

@Value
@Builder(toBuilder = true)
public class CommentCreationDto {
    String text;
}
