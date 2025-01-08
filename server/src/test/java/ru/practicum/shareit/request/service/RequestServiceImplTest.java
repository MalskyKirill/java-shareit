package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoResp;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class RequestServiceImplTest {
    private final RequestRepository requestRepository = mock(RequestRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final RequestService requestService = new RequestServiceImpl(requestRepository, userRepository,
        itemRepository);
    private static ItemRequest itemRequest;
    private static ItemRequestDtoResp itemRequestDto;
    private static User user;

    @BeforeAll
    static void setUp() {
        user = new User(1L, "name", "user1@mail.com");
        itemRequest = new ItemRequest(1L, "description", user, LocalDateTime.now());
        itemRequestDto = ItemRequestMapper.mapToItemRequestDtoResp(itemRequest);
    }

    @Test
    void getAllRequestsByOwner() {
        when(requestRepository.findAllByRequestorId(anyLong(), any(Sort.class)))
            .thenReturn(List.of(itemRequest));
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(user));
        List<ItemRequestDtoResp> result = requestService.getAllRequestsByOwner(1L);
        assertNotNull(result);
        assertEquals(List.of(itemRequestDto), result);
        verify(requestRepository, times(1)).findAllByRequestorId(anyLong(), any(Sort.class));
        verify(itemRepository, times(1)).findAllByItemRequestIn(anyList());
    }

    @Test
    void getRequestById() {
        when(requestRepository.findById(anyLong()))
            .thenReturn(Optional.of(itemRequest));
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(user));
        ItemRequestDtoResp result = requestService.getRequestById(1L, 1L);
        result.setItems(null);
        assertNotNull(result);
        assertEquals(itemRequestDto, result);
        verify(requestRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).findAllByItemRequest(any(ItemRequest.class));
    }

}
