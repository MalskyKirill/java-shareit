package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto createItem(Item item, Long ownerId) {
        userStorage.checkUser(ownerId);
        item.setOwnerId(ownerId);
        return ItemMapper.mapToItemDto(itemStorage.create(item));
    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) {
        userStorage.checkUser(userId);
        return ItemMapper.mapToItemDto(itemStorage.get(itemId));
    }

    @Override
    public List<ItemDto> getAllItemsByUser(Long userId) {
        userStorage.checkUser(userId);

        List<Item> items = itemStorage.getAllItemsByUser(userId); // получаем айтемы юзера

        List<ItemDto> itemsDto = items.stream().map(ItemMapper::mapToItemDto).toList(); // бежим по коллекции item и мапим каждый в itemDto
        return itemsDto;
    }

    @Override
    public List<ItemDto> getSearchItemList(String text) {
        if (Objects.equals(text, "")) { // если передана пустая строка
            return new ArrayList<>(); // возвращаем пустой лист
        }

        List<Item> items = itemStorage.getSearchItemList(text.toLowerCase());
        List<ItemDto> itemsDto = items.stream().map(ItemMapper::mapToItemDto).toList();
        return itemsDto;
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, Item item) {
        userStorage.checkUser(userId);
        item.setOwnerId(userId);
        item.setId(itemId);
        return ItemMapper.mapToItemDto(itemStorage.update(item));
    }
}
