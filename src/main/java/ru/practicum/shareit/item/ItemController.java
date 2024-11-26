package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createNewItem(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.createItem(item, ownerId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getUserItem(@RequestHeader("X-Sharer-User-Id") Long userId, @PathVariable Long itemId) {
        return itemService.getItem(userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getAllItemsByUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam String text) {
        return itemService.getSearchItemList(text);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader ("X-Sharer-User-Id") Long userId
                                , @PathVariable Long itemId
                                , @RequestBody Item item) {
        return itemService.updateItem(userId, itemId, item);
    }

}
