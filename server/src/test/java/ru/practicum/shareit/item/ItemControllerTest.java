package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private static final String USER_ID = "X-Sharer-User-Id";

    private User user = new User(1L, "Kirill", "kirill@shareit.ru");

    private Item item = new Item(1L, "Item1", "Description1", true, user, null);

    private ItemDto itemDto = new ItemDto(1L, "Item1", "Description1", true, null);
    private ItemDtoWithBookingAndComments itemDtoWithBookingAndComments = new ItemDtoWithBookingAndComments(1L, "Item1", "Description1", true,
        null, null, null, null);

    private List listItemDto = new ArrayList<>();

    @Test
    void createNewItem() throws Exception {
        when(itemService.createItem(any(), any(Long.class)))
            .thenReturn(itemDto);

        mvc.perform(post("/items")
                .content(mapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
            .andExpect(jsonPath("$.name", is(itemDto.getName())))
            .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
            .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void getUserItem() throws Exception {
        when(itemService.getItem(any(Long.class), any(Long.class)))
            .thenReturn(itemDtoWithBookingAndComments);
        mvc.perform(get("/items/1")
                .content(mapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
            .andExpect(jsonPath("$.name", is(itemDto.getName())))
            .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
            .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }

    @Test
    void getAllItemsByUser() throws Exception {
        when(itemService.getAllItemsByUser(any(Long.class)))
            .thenReturn(List.of(itemDtoWithBookingAndComments));
        mvc.perform(get("/items")
                .content(mapper.writeValueAsString(listItemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
            .andExpect(jsonPath("$.[0].name", is(itemDto.getName())))
            .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription())))
            .andExpect(jsonPath("$.[0].available", is(itemDto.getAvailable())));
    }

    @Test
    void getItemsBySearch() throws Exception {
        when(itemService.getSearchItemList(any(String.class)))
            .thenReturn(List.of(itemDto));
        mvc.perform(get("/items/search?text=description")
                .content(mapper.writeValueAsString(listItemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
            .andExpect(jsonPath("$.[0].name", is(itemDto.getName())))
            .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription())))
            .andExpect(jsonPath("$.[0].available", is(itemDto.getAvailable())));
    }

    @Test
    void updateItem() throws Exception {
        when(itemService.updateItem(any(Long.class), any(Long.class), any()))
            .thenReturn(itemDto);
        mvc.perform(patch("/items/1")
                .content(mapper.writeValueAsString(itemDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(USER_ID, 1))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
            .andExpect(jsonPath("$.name", is(itemDto.getName())))
            .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
            .andExpect(jsonPath("$.available", is(itemDto.getAvailable())));
    }
}
