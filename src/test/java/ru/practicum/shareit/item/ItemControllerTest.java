package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.item.dto.CommentCreationDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtendedDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService service;

    @BeforeEach
    void setupEntities() {
        user = prepareDetachedUser();
        user2 = prepareDetachedUser();

        item = prepareDetachedItem(user, true);
        item2 = prepareDetachedItem(user, true);

    }

    @Test
    void addNewItem() throws Exception {
        ItemDto itemDto = ItemMapper.toItemDto(prepareDetachedItem(user, true));

        when(service.addItem(anyInt(), eq(itemDto)))
                .thenReturn(itemDto);

        mockMvc.perform(post("/items")
                .content(objectMapper.writeValueAsString(itemDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));

    }

    @Test
    void amendItem() throws Exception {
        ItemDto itemDto = ItemMapper.toItemDto(prepareDetachedItem(user, true));

        when(service.amendItem(anyInt(), anyInt(), eq(itemDto)))
                .thenReturn(itemDto);

        mockMvc.perform(patch("/items/1")
                .content(objectMapper.writeValueAsString(itemDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    @Test
    void getItem() throws Exception {
        ItemExtendedDto itemDto = ItemMapper.toItemExtendedDto(prepareDetachedItem(user, true));

        when(service.getItem(anyInt(), anyInt()))
                .thenReturn(itemDto);

        mockMvc.perform(get("/items/1")
                .content(objectMapper.writeValueAsString(itemDto))
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(itemDto)));
    }

    @Test
    void getAllItemsByOwner() throws Exception {
        List<ItemExtendedDto> dtoList = List.of(
                ItemMapper.toItemExtendedDto(item),
                ItemMapper.toItemExtendedDto(item2)
        );

        when(service.getAllItemsByOwner(anyInt(), anyInt(), anyInt()))
                .thenReturn(dtoList);

        mockMvc.perform(get("/items/?from=0&size=10")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dtoList)));
    }

    @Test
    void searchItem() throws Exception {
        List<ItemDto> dtoList = List.of(
                ItemMapper.toItemDto(item),
                ItemMapper.toItemDto(item2)
        );

        when(service.search(anyString(), anyInt(), anyInt()))
                .thenReturn(dtoList);

        mockMvc.perform(get("/items/search/")
                        .param("from", "0")
                        .param("size", "10")
                        .param("text", "test")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dtoList)));
    }

    @Test
    void addNewComment() throws Exception {
        CommentCreationDto creationDto = new CommentCreationDto();
        creationDto.setText("test comment");

        Comment comment = CommentMapper.toComment(creationDto);
        comment.setAuthor(user);

        when(service.addNewComment(anyInt(), anyInt(), eq(creationDto)))
                .thenReturn(CommentMapper.toCommentDto(comment));

        mockMvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(creationDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(creationDto)));
    }
}