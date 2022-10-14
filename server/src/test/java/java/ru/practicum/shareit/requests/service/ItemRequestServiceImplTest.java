package java.ru.practicum.shareit.requests.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.BaseTest;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.requests.dao.ItemRequestDAO;
import ru.practicum.shareit.requests.dto.ItemRequestCreationDto;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestExtendedDto;
import ru.practicum.shareit.requests.mapper.ItemRequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.service.ItemRequestService;
import ru.practicum.shareit.user.dao.UserDAO;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ItemRequestServiceImplTest extends BaseTest {

    @MockBean
    private ItemRequestDAO itemRequestDAO;

    @MockBean
    private UserDAO userDAO;

    @MockBean
    private ItemDAO itemDAO;

    @Autowired
    private ItemRequestService service;

    @BeforeEach
    void prepareEntities() {
        user = prepareDetachedUser();
        user.setId(1);

        user2 = prepareDetachedUser();
        user2.setId(2);

        user3 = prepareDetachedUser();
        user3.setId(3);

        dummy = prepareDetachedUser();
        dummy.setId(4);

        item = prepareDetachedItem(user3, true);
        item1 = prepareDetachedItem(dummy, true);

        itemRequest1 = prepareDetachedItemRequest(user);
        itemRequest2 = prepareDetachedItemRequest(user);
        itemRequest3 = prepareDetachedItemRequest(user2);

        when(itemRequestDAO.save(any(ItemRequest.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        when(userDAO.findById(user.getId()))
                .thenReturn(Optional.of(user));

        when(userDAO.findById(user2.getId()))
                .thenReturn(Optional.of(user2));

        when(itemRequestDAO.getItemRequestsByRequestor(user.getId()))
                .thenReturn(List.of(itemRequest1, itemRequest2));
    }

    @Test
    void createRequest() {
        ItemRequestCreationDto creationDto = ItemRequestCreationDto.builder()
                .description("Test")
                .build();

        ItemRequestDto result = service.createRequest(user.getId(), creationDto);
        assertEquals(user.getId(), result.getRequestorId());
        assertEquals("Test", result.getDescription());
    }

    @Test
    void getPersonalItemRequests() {
        itemRequest1.setId(1);
        itemRequest2.setId(2);

        item.setRequest(itemRequest1.getId());
        item1.setRequest(itemRequest2.getId());

        when(itemDAO.findAllByRequest(1))
                .thenReturn(List.of(item));

        when(itemDAO.findAllByRequest(2))
                .thenReturn(List.of(item1));

        List<ItemRequestExtendedDto> dto = List.of(
                ItemRequestMapper.toItemRequestExtendedDto(itemRequest1, List.of(item)),
                ItemRequestMapper.toItemRequestExtendedDto(itemRequest2, List.of(item1))
        );

        List<ItemRequestExtendedDto> result = service.getPersonalItemRequests(user.getId());
        assertEquals(dto, result);
    }

    @Test
    void getAllItemRequestsPaged() {
        itemRequest3.setId(1);

        when(itemDAO.findAllByRequest(1))
                .thenReturn(List.of(item));

        when(itemRequestDAO.getAllItemRequests(any()))
                .thenReturn(List.of(itemRequest1, itemRequest2, itemRequest3));

        List<ItemRequestExtendedDto> result = service.getAllItemRequestsPaged(user.getId(), 0, 10);
        assertEquals(1, result.size());
        assertTrue(result.contains(ItemRequestMapper.toItemRequestExtendedDto(itemRequest3, List.of(item))));
    }

    @Test
    void getItemRequestById() {
        itemRequest1.setId(1);

        when(itemDAO.findAllByRequest(1))
                .thenReturn(List.of(item));

        when(itemRequestDAO.findById(itemRequest1.getId()))
                .thenReturn(Optional.of(itemRequest1));

        ItemRequestExtendedDto result = service.getItemRequestById(user.getId(), itemRequest1.getId());
        assertEquals(ItemRequestMapper.toItemRequestExtendedDto(itemRequest1, List.of(item)), result);
    }
}