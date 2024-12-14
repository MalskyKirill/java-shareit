package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long ownerId);

    ItemDtoWithBookingAndComments getItem(Long userId, Long itemId);

    List<ItemDtoWithBookingAndComments> getAllItemsByUser(Long userId);

    List<ItemDto> getSearchItemList(String text);

    ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto);
}
