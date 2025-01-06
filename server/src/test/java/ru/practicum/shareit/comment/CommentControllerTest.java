package ru.practicum.shareit.comment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.comment.service.CommentService;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;

@WebMvcTest(controllers = CommentController.class)
class CommentControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    CommentService commentService;

    @Autowired
    private MockMvc mvc;

    private User user = new User(1L, "Kirill", "kirill@shareit.ru");

    private CommentDtoResponse commentDto = new CommentDtoResponse(1L, "Text comment",
        user.getName(), LocalDateTime.of(2024, 3, 5, 1, 2, 3));

    @Test
    void createNewComment() throws Exception {
        when(commentService.createComment(any(), any(Long.class), any(Long.class)))
            .thenReturn(commentDto);
        mvc.perform(post("/items/1/comment")
                .content(mapper.writeValueAsString(commentDto))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("X-Sharer-User-Id", 1))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
            .andExpect(jsonPath("$.text", is(commentDto.getText())))
            .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
            .andExpect(jsonPath("$.created",
                is(commentDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))));
    }
}
