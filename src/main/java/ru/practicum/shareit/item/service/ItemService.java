package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
     ItemDto createItem(Item item, Long ownerId);

    ItemDto getItem(Long userId, Long itemId);

    List<ItemDto> getAllItemsByUser(Long userId);

    List<ItemDto> getSearchItemList(String text);

    ItemDto updateItem(Long userId, Long itemId, Item item);
}
