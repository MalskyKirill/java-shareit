package ru.practicum.shareit.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.comment.service.CommentService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/items/{itemId}/comment")
    public CommentDtoResponse createNewComment(@Valid @RequestBody CommentDto commentDto, @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("POST-запрос к эндпоинту: '/items/{itemId}/comment' на добавление comment у user с ID={}", userId);
        return commentService.createComment(commentDto, userId, itemId);
    }
}
