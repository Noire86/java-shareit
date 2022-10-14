package java.ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    @Test
    void createWithInvalidItem() throws Exception {
        BookingCreationDto dto = BookingCreationDto.builder()
                .itemId(404)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

        mockMvc.perform(
                        post("/create")
                                .header("X-Sharer-User-Id", 1)
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void createWithValidDto() throws Exception {
        BookingCreationDto dto = BookingCreationDto.builder()
                .itemId(1)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

        when(bookingService.create(anyInt(), eq(dto)))
                .thenReturn(new BookingDto());

        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 1)
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void createWithInvalidDto() throws Exception {
        BookingCreationDto dto = new BookingCreationDto();
        mockMvc.perform(
                        post("/bookings")
                                .header("X-Sharer-User-Id", 1)
                                .content(objectMapper.writeValueAsString(dto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findBookingByValidId() throws Exception {
        BookingDto dto = new BookingDto();
        when(bookingService.findBookingById(anyInt(), anyInt()))
                .thenReturn(dto);

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void findBookingByInvalidId() throws Exception {
        when(bookingService.findBookingById(anyInt(), eq(404)))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/bookings/404")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    void getRequestorBookings() throws Exception {
        List<BookingDto> dto = Collections.singletonList(new BookingDto());
        when(bookingService.getRequestorBookings(anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(dto);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "WAITING")
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void getOwnerBookings() throws Exception {
        List<BookingDto> dto = Collections.singletonList(new BookingDto());
        when(bookingService.getOwnerBookings(eq(68), anyString(), anyInt(), anyInt()))
                .thenReturn(dto);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 68)
                        .param("state", "WAITING")
                        .param("from", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }

    @Test
    void approveRequest() throws Exception {
        BookingDto dto = new BookingDto();

        when(bookingService.approveRequest(anyInt(), anyInt(), anyBoolean()))
                .thenReturn(dto);

        mockMvc.perform(patch("/bookings/1")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(dto)));
    }
}