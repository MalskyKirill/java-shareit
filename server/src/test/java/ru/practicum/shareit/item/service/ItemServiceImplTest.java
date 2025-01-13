package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.comment.repository.CommentRepository;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.comment.service.CommentServiceImpl;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ItemServiceImplTest {
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
    private static ItemDtoWithBookingAndComments itemDtoWithBookingAndComments;

    @BeforeAll
    static void setUp() {
        user = new User(1L, "Kirill", "kirill@shareit.ru");
        item = new Item(1L, "Item1", "Description1", true, user, null);
        itemDto = ItemMapper.mapToItemDto(item);
        itemDtoWithBookingAndComments = ItemMapper.mapToItemDtoWithBookingAndComments(item, null, null, null);
    }

    @Test
    void createItem() {
        when(itemRepository.save(any(Item.class)))
            .thenReturn(item);
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(user));
        ItemDto result = itemService.createItem(itemDto, 1L);
        assertNotNull(result);
        assertEquals(itemDto, result);
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateItem() {
        Item newItem = new Item(2L, "item", "best", true, user, null);
        ItemDto itemUpdate = new ItemDto(2L, "newName", "newDis", true, null);
        when(itemRepository.findById(anyLong()))
            .thenReturn(Optional.of(newItem));
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class)))
            .thenReturn(ItemMapper.mapToItem(itemUpdate, user));
        ItemDto result = itemService.updateItem(user.getId(), itemUpdate.getId(), itemUpdate);
        assertNotNull(result);
        assertEquals(itemUpdate, result);
        verify(itemRepository, times(1)).save(any(Item.class));
        verify(itemRepository, times(1)).findById(anyLong());
    }

    @Test
    void getSearchItemList() {
        when(itemRepository.getItemsBySearchQuery(anyString()))
            .thenReturn(List.of(item));
        List<ItemDto> result = itemService.getSearchItemList("item");
        assertNotNull(result);
        assertEquals(List.of(itemDto), result);
        verify(itemRepository, times(1))
            .getItemsBySearchQuery(anyString());
    }

    @Test
    void getAllItemsByUser() {
        when(itemRepository.findItemsByOwnerId(anyLong()))
            .thenReturn(List.of(item));
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(user));
        List<ItemDtoWithBookingAndComments> result = itemService.getAllItemsByUser(1L);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(List.of(itemDtoWithBookingAndComments), result);
        verify(itemRepository, times(1)).findItemsByOwnerId(anyLong());
    }

    @Test
    void shouldExceptionWhenGetItemWithFaiItem() {
        NotFoundException exp = assertThrows(NotFoundException.class,
            () -> itemService.getItem(1L, 1L));
        assertEquals("Item with id 1 not found",
            exp.getMessage());
    }
}
