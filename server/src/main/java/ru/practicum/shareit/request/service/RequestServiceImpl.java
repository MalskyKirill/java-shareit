package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResp;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService{
    private final RequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

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
    @Transactional(readOnly = true)
    public List<ItemRequestDtoResp> getAllRequestsByOwner(Long userId) {
        getUser(userId);

        List<ItemRequest> requestsList = repository.findAllByRequestorId(userId, sortByDesc);
        log.info("requestsList on the user " + userId + " have been received from bd");

        List<Item> itemsList = itemRepository.findAllByItemRequestIn(requestsList);

        List<ItemRequestDtoResp> response = new ArrayList<>();

        for (ItemRequest request : requestsList) {
            response.add(ItemRequestMapper.mapToItemRequestDtoResp(request, itemsList.stream().filter(item -> Objects.equals(item.getItemRequest().getId(), request.getId())).collect(Collectors.toList())));
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDtoResp getRequestById(Long userId, Long requestId) {
        getUser(userId);

        ItemRequest itemRequest = repository.findById(requestId).orElseThrow(() -> {
            log.error("request with id " + requestId + " not found");
            throw new NotFoundException("request with id " + requestId + " not found");
        });

        log.info("request on the user " + userId + " have been received from bd");
        List<Item> items = itemRepository.findAllByItemRequest(itemRequest);
        return ItemRequestMapper.mapToItemRequestDtoResp(itemRequest, items);
    }


    private User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> {
            log.error("User with id " + userId + " not found");
            throw new NotFoundException("User with id " + userId + " not found");
        });
    }
}
