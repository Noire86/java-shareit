package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreationDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void getRequestorBookings() {
    }

    @Test
    void getOwnerBookings() {
    }

    @Test
    void approveRequest() {
    }
}