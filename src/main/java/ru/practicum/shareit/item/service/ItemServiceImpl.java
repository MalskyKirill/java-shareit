package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto createItem(Item item, Long ownerId) {
        userStorage.userCheck(ownerId);
        item.setOwnerId(ownerId);
        return ItemMapper.mapToItemDto(itemStorage.create(item));
    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) {
        userStorage.userCheck(userId);
        return ItemMapper.mapToItemDto(itemStorage.get(itemId));
    }
}
