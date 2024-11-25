package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public List<ItemDto> getAllItemsByUser(Long userId) {
        userStorage.userCheck(userId);

        List<Item> items = itemStorage.getAllItemsByUser(userId); // получаем айтемы юзера
        if (items == null) {
            return null;
        }
        List<ItemDto> itemsDto = items.stream().map(ItemMapper::mapToItemDto).toList(); // бежим по коллекции item и мапим каждый в itemDto
        return itemsDto;
    }
}
