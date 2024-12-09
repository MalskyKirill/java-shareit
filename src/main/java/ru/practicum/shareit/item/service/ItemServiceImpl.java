package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
//import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
//    private final UserStorage userStorage;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
//        userStorage.checkUser(ownerId);
//        Item item = ItemMapper.mapToItem(itemDto, ownerId);
//        ItemDto newItemDto = ItemMapper.mapToItemDto(itemStorage.create(item));
//        log.info("создан новый item с ID = {}", newItemDto.getId());
//        return newItemDto;
        return null;
    }

    @Override
    public ItemDto getItem(Long userId, Long itemId) {
//        userStorage.checkUser(userId);
        ItemDto itemDto = ItemMapper.mapToItemDto(itemStorage.get(itemId));
        log.info("получен item с ID = {}", itemDto.getId());
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllItemsByUser(Long userId) {
//        userStorage.checkUser(userId);

        List<Item> items = itemStorage.getAllItemsByUser(userId); // получаем айтемы юзера
        if (items == null) {
            log.info("пользователя с ID = {} список вещей пуст", userId);
            return null;
        }

        List<ItemDto> itemsDto = items.stream()
            .map(ItemMapper::mapToItemDto)
            .toList(); // бежим по коллекции item и мапим каждый в itemDto

        log.info("получен список item у пользователя с ID = {}", userId);
        return itemsDto;
    }

    @Override
    public List<ItemDto> getSearchItemList(String text) {
        if (Objects.equals(text, "")) { // если передана пустая строка
            return new ArrayList<>(); // возвращаем пустой лист
        }

        List<Item> items = itemStorage.getSearchItemList(text.toLowerCase());
        List<ItemDto> itemsDto = items.stream()
            .map(ItemMapper::mapToItemDto)
            .toList();

        log.info("получен список item по запросу = {}", text);
        return itemsDto;
    }

    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
//        userStorage.checkUser(userId);
//        Item item = ItemMapper.mapToItem(itemDto, userId);
//        item.setOwnerId(userId);
//        item.setId(itemId);
//        ItemDto updateItemDto = ItemMapper.mapToItemDto(itemStorage.update(item));
//        log.info("обновлен item с ID = {}", itemId);
//        return updateItemDto;
        return null;
    }
}
