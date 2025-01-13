package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResp;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;

import static org.hamcrest.Matchers.is;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    RequestService requestService;

    @Autowired
    private MockMvc mvc;

    private static final String USER_ID = "X-Sharer-User-Id";

    private UserDto userDto = new UserDto(1L, "Kirill", "kirill@shareit.ru");
    private ItemRequestDto itemRequestDto = new ItemRequestDto("ItemRequest description");

    private ItemRequestDtoResp itemRequestDtoResp = new ItemRequestDtoResp(1L, "ItemRequest description",
        userDto, LocalDateTime.of(2025, 1, 2, 3, 4, 5), null);

    private List<ItemRequestDtoResp> listItemRequestDto = new ArrayList<>();

    @Test
    void createNewRequest() throws Exception {
        when(requestService.createRequest(any(), any(Long.class)))
            .thenReturn(itemRequestDtoResp);
        mvc.perform(post("/requests")
                .content(mapper.writeValueAsString(itemRequestDtoResp))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_ID, 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(itemRequestDtoResp.getId()), Long.class))
            .andExpect(jsonPath("$.description", is(itemRequestDtoResp.getDescription())))
            .andExpect(jsonPath("$.requestor.id", is(itemRequestDtoResp.getRequestor().getId()), Long.class))
            .andExpect(jsonPath("$.requestor.name", is(itemRequestDtoResp.getRequestor().getName())))
            .andExpect(jsonPath("$.requestor.email", is(itemRequestDtoResp.getRequestor().getEmail())))
            .andExpect(jsonPath("$.created",
                is(itemRequestDtoResp.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void getAllRequestsByOwner() throws Exception {
        when(requestService.getAllRequestsByOwner(any(Long.class)))
            .thenReturn(List.of(itemRequestDtoResp));
        mvc.perform(get("/requests")
                .content(mapper.writeValueAsString(listItemRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_ID, 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id", is(itemRequestDtoResp.getId()), Long.class))
            .andExpect(jsonPath("$.[0].description", is(itemRequestDtoResp.getDescription())))
            .andExpect(jsonPath("$.[0].requestor.id", is(itemRequestDtoResp.getRequestor().getId()), Long.class))
            .andExpect(jsonPath("$.[0].requestor.name", is(itemRequestDtoResp.getRequestor().getName())))
            .andExpect(jsonPath("$.[0].requestor.email", is(itemRequestDtoResp.getRequestor().getEmail())))
            .andExpect(jsonPath("$.[0].created",
                is(itemRequestDtoResp.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void getAllRequestsCreatedOtherUsers() throws Exception {
        when(requestService.getAllRequestsCreatedOtherUsers(any(Long.class)))
            .thenReturn(List.of(itemRequestDtoResp));
        mvc.perform(get("/requests/all")
                .content(mapper.writeValueAsString(listItemRequestDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_ID, 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[0].id", is(itemRequestDtoResp.getId()), Long.class))
            .andExpect(jsonPath("$.[0].description", is(itemRequestDtoResp.getDescription())))
            .andExpect(jsonPath("$.[0].requestor.id", is(itemRequestDtoResp.getRequestor().getId()), Long.class))
            .andExpect(jsonPath("$.[0].requestor.name", is(itemRequestDtoResp.getRequestor().getName())))
            .andExpect(jsonPath("$.[0].requestor.email", is(itemRequestDtoResp.getRequestor().getEmail())))
            .andExpect(jsonPath("$.[0].created",
                is(itemRequestDtoResp.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }

    @Test
    void getRequestById() throws Exception {
        when(requestService.getRequestById(any(Long.class), any(Long.class)))
            .thenReturn(itemRequestDtoResp);
        mvc.perform(get("/requests/1")
                .content(mapper.writeValueAsString(itemRequestDtoResp))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_ID, 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(itemRequestDtoResp.getId()), Long.class))
            .andExpect(jsonPath("$.description", is(itemRequestDtoResp.getDescription())))
            .andExpect(jsonPath("$.requestor.id", is(itemRequestDtoResp.getRequestor().getId()), Long.class))
            .andExpect(jsonPath("$.requestor.name", is(itemRequestDtoResp.getRequestor().getName())))
            .andExpect(jsonPath("$.requestor.email", is(itemRequestDtoResp.getRequestor().getEmail())))
            .andExpect(jsonPath("$.created",
                is(itemRequestDtoResp.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }
}
