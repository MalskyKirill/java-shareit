package ru.practicum.shareit.comment;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class CommentController {
    private final CommentClient createComment;

    @PostMapping("/items/{itemId}/comment")
    public ResponseEntity<Object> createNewComment(@Valid @RequestBody CommentDto commentDto, @RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("POST-запрос к эндпоинту: '/items/{itemId}/comment' на добавление comment у user с ID={}", userId);
        return createComment.createComment(commentDto, userId, itemId);
    }
}
