package ru.practicum.shareit.requests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.requests.dto.ItemRequestCreationDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestExtendedDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.service.ItemRequestService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest extends BaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService service;

    @BeforeEach
    void prepareEntities() {
        user = prepareDetachedUser();
        user2 = prepareDetachedUser();

        itemRequest1 = prepareDetachedItemRequest(user);
        itemRequest2 = prepareDetachedItemRequest(user);
        itemRequest3 = prepareDetachedItemRequest(user2);
    }

    @Test
    void createRequest() throws Exception {
        ItemRequestCreationDto dto = ItemRequestCreationDto.builder()
                .description(itemRequest1.getDescription())
                .build();

        ItemRequestDto result = ItemRequestMapper.toItemRequestDto(itemRequest1);

        when(service.createRequest(1, dto))
                .thenReturn(result);

        mockMvc.perform(post("/requests")
                        .content(objectMapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }

    @Test
    void getPersonalItemRequests() throws Exception {
        List<ItemRequestExtendedDto> result = List.of(
                ItemRequestMapper.toItemRequestExtendedDto(itemRequest1, new ArrayList<>()),
                ItemRequestMapper.toItemRequestExtendedDto(itemRequest2, new ArrayList<>())
        );

        when(service.getPersonalItemRequests(1))
                .thenReturn(result);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }

    @Test
    void getAllItemRequests() throws Exception {
        List<ItemRequestExtendedDto> result = List.of(
                ItemRequestMapper.toItemRequestExtendedDto(itemRequest1, new ArrayList<>()),
                ItemRequestMapper.toItemRequestExtendedDto(itemRequest2, new ArrayList<>()),
                ItemRequestMapper.toItemRequestExtendedDto(itemRequest3, new ArrayList<>())
        );

        when(service.getAllItemRequestsPaged(1, 0, 10))
                .thenReturn(result);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }

    @Test
    void getItemRequestById() throws Exception {
        ItemRequestExtendedDto result = ItemRequestMapper.toItemRequestExtendedDto(itemRequest1, new ArrayList<>());

        when(service.getItemRequestById(anyInt(), anyInt()))
                .thenReturn(result);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(result)));
    }
}