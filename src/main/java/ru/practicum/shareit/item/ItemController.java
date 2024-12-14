package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createNewItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("POST-запрос к эндпоинту: '/items' на добавление item у user с ID={}", ownerId);
        return itemService.createItem(itemDto, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDtoWithBookingAndComments getUserItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        log.info("GET-запрос к эндпоинту: '/items' на получение item с ID={}", itemId);
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDtoWithBookingAndComments> getAllItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("GET-запрос к эндпоинту: '/items' на получение всех item у user с ID={}", userId);
        return itemService.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam String text) {
        log.info("GET-запрос к эндпоинту: '/items/search' на поиск item с текстом={}", text);
        return itemService.getSearchItemList(text);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId, @RequestBody ItemDto itemDto) {
        log.info("PATCH-запрос к эндпоинту: '/items' на обновление item с ID={}", itemId);
        return itemService.updateItem(userId, itemId, itemDto);
    }

}
