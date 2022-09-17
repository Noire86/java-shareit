package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentItemDto;
import ru.practicum.shareit.item.model.Comment;

public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .item(comment.getItem())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .build();
    }

    public static Comment toComment(CommentCreationDto creationDto) {
        Comment comment = new Comment();
        comment.setText(creationDto.getText());

        return comment;
    }

    public static CommentItemDto toCommentItemDto(Comment comment) {
        return CommentItemDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }
}
