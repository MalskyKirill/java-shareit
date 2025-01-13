package ru.practicum.shareit.comment.mapper;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static Comment mapToComment(CommentDto commentDto, Item item, User author, LocalDateTime now) {
        Comment comment = new Comment();
        comment.setText(commentDto.getText());
        comment.setItem(item);
        comment.setAuthor(author);
        comment.setCreated(now);
        return comment;
    }

    public static CommentDtoResponse mapToCommentDtoResponse(Comment comment) {
        CommentDtoResponse commentDtoResponse = new CommentDtoResponse();
        commentDtoResponse.setId(comment.getId());
        commentDtoResponse.setText(comment.getText());
        commentDtoResponse.setAuthorName(comment.getAuthor().getName());
        commentDtoResponse.setCreated(comment.getCreated());
        return commentDtoResponse;
    }
}
