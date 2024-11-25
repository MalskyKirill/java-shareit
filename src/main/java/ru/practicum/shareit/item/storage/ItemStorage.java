package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item create(Item item);

    Item get(Long itemId);

    List<Item> getAllItemsByUser(Long userId);

    List<Item> getSearchItemList(String text);
}
