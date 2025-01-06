package ru.practicum.shareit.comment.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserServiceImpl(userRepository);
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final RequestRepository requestRepository = mock(RequestRepository.class);
    private final BookingRepository bookingRepository = mock(BookingRepository.class);
    private final CommentService commentService = new CommentServiceImpl(userRepository, itemRepository, bookingRepository, commentRepository);
    private final BookingService bookingService = new BookingServiceImpl(bookingRepository, userRepository, itemRepository);
    private final ItemService itemService = new ItemServiceImpl(itemRepository, userService, bookingService, commentService, requestRepository);

    private static User user;
    private static Item item;
    private static ItemDto itemDto;


    @BeforeAll
    static void setUp() {
        user = new User(1L, "Kirill", "kirill@shareit.ru");
        item = new Item(1L, "Item1", "Description1", true, user, null);
        itemDto = ItemMapper.mapToItemDto(item);
    }

    @Test
    void createComment() {
        Comment comment = new Comment(1L, "text", item, user, null);
        CommentDtoResponse commentDtoR = CommentMapper.mapToCommentDtoResponse(comment);
        CommentDto commentDto = new CommentDto("text");
        Booking booking = new Booking(1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1), item, user, BookingStatus.APPROVED);
        when(commentRepository.save(any(Comment.class)))
            .thenReturn(comment);
        when(itemRepository.findById(anyLong()))
            .thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(user));
        when(bookingRepository.findByItemIdAndBookerIdAndStatusAndStartBefore(anyLong(), anyLong(),
            any(BookingStatus.class), any(LocalDateTime.class)))
            .thenReturn(booking);
        CommentDtoResponse result = commentService.createComment(commentDto, user.getId(), item.getId());
        result.setId(1L);
        assertNotNull(result);
        commentDtoR.setCreated(result.getCreated());
        assertEquals(commentDtoR, result);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }
}
