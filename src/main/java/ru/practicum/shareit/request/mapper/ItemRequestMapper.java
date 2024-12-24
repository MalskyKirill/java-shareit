package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResp;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class ItemRequestMapper {

    public static ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto, User requestor, LocalDateTime created) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(created);

        return itemRequest;
    }

    public static ItemRequestDtoResp mapToItemRequestDtoResp(ItemRequest itemRequest) {
        ItemRequestDtoResp itemRequestDtoResp = new ItemRequestDtoResp();
        itemRequestDtoResp.setId(itemRequest.getId());
        itemRequestDtoResp.setDescription(itemRequest.getDescription());
        itemRequestDtoResp.setRequestor(UserMapper.mapToUserDto(itemRequest.getRequestor()));
        itemRequestDtoResp.setCreated(itemRequest.getCreated());

        return itemRequestDtoResp;
    }
}
