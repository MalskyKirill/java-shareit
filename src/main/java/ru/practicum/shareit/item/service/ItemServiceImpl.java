package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{
    private final ItemStorage itemStorage;

    @Override
    public ItemDto createItem(Item item) {
        return itemStorage.create()
    }
}
