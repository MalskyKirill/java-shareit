package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface CommentService {
    CommentDtoResponse createComment(CommentDto commentDto, Long userId, Long itemId);

    List<CommentDtoResponse> getAllCommentsByItemId(Long itemId);

    Map<Item, List<CommentDtoResponse>> getAllCommentsBySomeItems(List<Long> itemsId);
}
