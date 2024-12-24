package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResp;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService{
    private final RequestRepository repository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestDtoResp createRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id " + userId + " not found");
            throw new NotFoundException("User with id " + userId + " not found");
        });

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto, user, LocalDateTime.now());
        ItemRequestDtoResp itemRequestDtoResp = ItemRequestMapper.mapToItemRequestDtoResp(repository.save(itemRequest));

        log.info("создан новый request с ID = {}", itemRequestDtoResp.getId());
        return itemRequestDtoResp;
    }


}
