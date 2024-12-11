package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Transactional
    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = UserMapper.mapToUser(userService.getUser(ownerId));
        Item item = ItemMapper.mapToItem(itemDto, owner);
        ItemDto newItemDto = ItemMapper.mapToItemDto(itemRepository.save(item));
        log.info("создан новый item с ID = {}", newItemDto.getId());
        return newItemDto;
    }

    @Transactional(readOnly = true)
    @Override
    public ItemDto getItem(Long userId, Long itemId) {
        ItemDto itemDto = ItemMapper.mapToItemDto(itemRepository.findById(itemId).orElseThrow(() -> {
            log.error("Item with id " + itemId + " not found");
            throw new NotFoundException("Item with id " + itemId + " not found");
        }));
        log.info("получен item с ID = {}", itemDto.getId());
        return itemDto;
    }

    @Override
    public List<ItemDto> getAllItemsByUser(Long userId) {
        userService.getUser(userId);
        List<Item> items = itemRepository.findItemsByOwnerId(userId);
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

        List<Item> items = itemRepository.getItemsBySearchQuery(text.toLowerCase());
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
        userService.getUser(userId);

        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            log.error("Item with id " + itemId + " not found");
            throw new NotFoundException("Item with id " + itemId + " not found");
        });

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        ItemDto updateItemDto = ItemMapper.mapToItemDto(itemRepository.save(item));
        log.info("обновлен item с ID = {}", itemId);
        return updateItemDto;
    }
}
