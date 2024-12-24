package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResp;

public interface RequestService {
    ItemRequestDtoResp createRequest(ItemRequestDto itemRequestDto, Long userId);
}
