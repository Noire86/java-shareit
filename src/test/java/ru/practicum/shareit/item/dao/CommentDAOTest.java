package ru.practicum.shareit.item.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class CommentDAOTest extends BaseTest {

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private TestEntityManager em;

    @BeforeEach
    void prepareEntities() {
        user = em.persistFlushFind(prepareDetachedUser());
        item = em.persistFlushFind(prepareDetachedItem(user, true));

        comment1 = em.persistFlushFind(prepareDetachedComment(item, user));
        comment2 = em.persistFlushFind(prepareDetachedComment(item, user));
    }

    @Test
    void getCommentsByItemId() {
        List<Comment> result = commentDAO.getCommentsByItemId(item.getId());

        assertEquals(result.size(), 2);
        assertTrue(result.contains(comment1));
        assertTrue(result.contains(comment2));
    }
}