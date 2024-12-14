package ru.practicum.shareit.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentDtoResponse createComment(CommentDto commentDto, Long userId, Long itemId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id " + userId + " not found");
            throw new NotFoundException("User with id " + userId + " not found");
        });
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            log.error("Item with id " + itemId + " not found");
            throw new NotFoundException("Item with id " + itemId + " not found");
        });

        Booking booking = bookingRepository.findByItemIdAndBookerIdAndStatusAndStartBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());

        if (booking == null) {
            log.error("The user did not book this item");
            throw new ValidationException("The user did not book this item");
        }

        Comment comment = CommentMapper.mapToComment(commentDto, item, user, LocalDateTime.now());
        commentRepository.save(comment);

        log.info("A user's " + userId + " comment on the item " + itemId + " was created");
        return CommentMapper.mapToCommentDtoResponse(comment);
    }

    @Override
    public List<CommentDtoResponse> getAllCommentsByItemId(Long itemId) {
        List<Comment> comments = commentRepository.findAllByItemId(itemId);
        log.info("Comments on the item " + itemId + " have been received from bd");
        return comments.stream().map(CommentMapper::mapToCommentDtoResponse).collect(Collectors.toList());
    }
}
