package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResp;

import java.util.List;

public interface RequestService {
    ItemRequestDtoResp createRequest(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDtoResp> getAllRequestsByOwner(Long userId);

    ItemRequestDtoResp getRequestById(Long userId, Long requestId);

    List<ItemRequestDtoResp> getAllRequestsCreatedOtherUsers(Long userId);
}
