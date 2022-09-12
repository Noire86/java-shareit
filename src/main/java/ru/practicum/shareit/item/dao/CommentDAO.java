package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

/**
 * Репозиторий комментариев
 */
public interface CommentDAO extends JpaRepository<Comment, Integer> {

    @Query("select c from Comment c where c.item.id = ?1")
    List<Comment> getCommentsByItemId(Integer itemId);
}
