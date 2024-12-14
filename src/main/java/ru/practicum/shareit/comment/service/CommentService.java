package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;

import java.util.List;

public interface CommentService {
    CommentDtoResponse createComment(CommentDto commentDto, Long userId, Long itemId);

    List<CommentDtoResponse> getAllCommentsByItemId(Long itemId);
}
