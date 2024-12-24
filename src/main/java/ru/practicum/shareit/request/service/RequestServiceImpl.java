package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService{
    private final RequestRepository repository;
    private final UserRepository userRepository;

    private final Sort sortByDesc = Sort.by(Sort.Direction.DESC, "created");

    @Override
    @Transactional
    public ItemRequestDtoResp createRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = getUser(userId);

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(itemRequestDto, user, LocalDateTime.now());
        ItemRequestDtoResp itemRequestDtoResp = ItemRequestMapper.mapToItemRequestDtoResp(repository.save(itemRequest));

        log.info("создан новый request с ID = {}", itemRequestDtoResp.getId());
        return itemRequestDtoResp;
    }

    @Override
    public List<ItemRequestDtoResp> getAllRequestsByOwner(Long userId) {
        getUser(userId);

        List<ItemRequest> requestsList = repository.findAllByRequestorId(userId, sortByDesc);
        log.info("requestsList on the user " + userId + " have been received from bd");

        return requestsList.stream().map(ItemRequestMapper::mapToItemRequestDtoResp).collect(Collectors.toList());
    }


    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id " + userId + " not found");
            throw new NotFoundException("User with id " + userId + " not found");
        });
    }
}
