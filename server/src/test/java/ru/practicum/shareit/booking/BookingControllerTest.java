package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.enums.BookingState;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private static final String USER_ID = "X-Sharer-User-Id";

    private BookingDtoRequest bookingDtoRequest = new BookingDtoRequest(
        1L,
        LocalDateTime.of(2030, 12, 25, 12, 00, 00),
        LocalDateTime.of(2030, 12, 26, 12, 00, 00));

    private BookingDto bookingDto = new BookingDto(
        1L,
        LocalDateTime.of(2030, 12, 25, 12, 00, 00),
        LocalDateTime.of(2030, 12, 26, 12, 00, 00),
        new Item(1L, "FirstItem", "DescriptionOfFirstItem", true,
            new User(1L, "FirstUser", "first@email.ru"), null),
        new User(2L, "SecondUser", "second@email.ru"), BookingStatus.WAITING);
    private List listBookingDto = new ArrayList<>();

    @Test
    void createNewBooking() throws Exception {
        when(bookingService.createBooking(any(), any(Long.class)))
            .thenReturn(bookingDto);

        mvc.perform(post("/bookings")
                .content(mapper.writeValueAsString(bookingDtoRequest))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_ID, 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
            .andExpect(jsonPath("$.start",
                is(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$.end",
                is(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void updateBooking() throws Exception {
        when(bookingService.updateBookingApproved(any(Long.class), any(Long.class), any(Boolean.class)))
            .thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1")
                .content(mapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_ID, 1)
                .queryParam("approved", "true"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
            .andExpect(jsonPath("$.start",
                is(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$.end",
                is(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getBooking(any(Long.class), any(Long.class)))
            .thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                .content(mapper.writeValueAsString(bookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_ID, 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
            .andExpect(jsonPath("$.start",
                is(bookingDto.getStart().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$.end",
                is(bookingDto.getEnd().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$.status", is(bookingDto.getStatus().toString()), BookingStatus.class));
    }

    @Test
    void getAllBookingByUser() throws Exception {
        when(bookingService.getAllBooking(any(Long.class), any(BookingState.class)))
            .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings")
                .content(mapper.writeValueAsString(listBookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_ID, 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class))
            .andExpect(jsonPath("$.[0].start", is(bookingDto.getStart()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$.[0].end", is(bookingDto.getEnd()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$.[0].item.id", is(bookingDto.getItem().getId()), Long.class))
            .andExpect(jsonPath("$.[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
            .andExpect(jsonPath("$.[0].status", is(bookingDto.getStatus().toString())));
    }

    @Test
    void getAllBookingByOwner() throws Exception {
        when(bookingService.getAllBookingByOwner(any(Long.class), any(BookingState.class)))
            .thenReturn(List.of(bookingDto));

        mvc.perform(get("/bookings/owner")
                .content(mapper.writeValueAsString(listBookingDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_ID, 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class))
            .andExpect(jsonPath("$.[0].start", is(bookingDto.getStart()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$.[0].end", is(bookingDto.getEnd()
                .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
            .andExpect(jsonPath("$.[0].item.id", is(bookingDto.getItem().getId()), Long.class))
            .andExpect(jsonPath("$.[0].booker.id", is(bookingDto.getBooker().getId()), Long.class))
            .andExpect(jsonPath("$.[0].status", is(bookingDto.getStatus().toString())));
    }
}
