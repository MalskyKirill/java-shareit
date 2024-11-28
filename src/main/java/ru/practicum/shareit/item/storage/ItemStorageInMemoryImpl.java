package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class ItemStorageInMemoryImpl implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> itemsByUser = new HashMap<>(); // создаем мапу для хранения item юзера

    @Override
    public Item create(Item item) {
        if (item.getId() != null && items.containsKey(item.getId())) {
            log.error("Item with id " + item.getId() + " already created");
            throw new AlreadyExistsException("Item with id " + item.getId() + " already created");
        }

        item.setId(getId());
        items.put(item.getId(), item);

        if (!itemsByUser.containsKey(item.getOwnerId())) { // ежели в мапе itemsByUser нет ключа userId
            itemsByUser.put(item.getOwnerId(), new ArrayList<>()); // добавляем в мапу новые ключ-лист
        }

        List<Item> userItems = itemsByUser.get(item.getOwnerId()); // берем список item юзера
        userItems.add(item); // добавляем новую вещь
        itemsByUser.put(item.getOwnerId(), userItems); // обнавляем мапу

        log.info("item с ID = {}, добавлен в HashMap", item.getId());
        return item;
    }

    @Override
    public Item get(Long itemId) {
        Item item = items.get(itemId);
        if (item == null) {
            log.error("Item with id " + itemId + " not found");
            throw new NotFoundException("Item with id " + itemId + " not found");
        }
        log.info("item с ID = {}, достали из HashMap", item.getId());
        return item;
    }

    @Override
    public List<Item> getAllItemsByUser(Long userId) {
        List<Item> itemList = itemsByUser.get(userId);
        log.info("список item у user с ID = {}, достали из HashMap", userId);
        return itemList;
    }

    @Override
    public List<Item> getSearchItemList(String text) {
        List<Item> itemList = items.values() // берем значения
            .stream() // преобразуем в стрим
            .filter(item -> item.getName().toLowerCase().contains(text) || item.getDescription().toLowerCase().contains(text)) // проверяем наличие подстроки в строке
            .filter(Item::getAvailable) // проверяем на доступность
            .collect(Collectors.toList()); // собираем в коллекцию
        log.info("список item по запросу {}, достали из HashMap", text);
        return itemList;
    }

    @Override
    public Item update(Item item) {
        checkItem(item.getId());

        if (item.getName() == null) {
            item.setName(items.get(item.getId()).getName());
        }

        if (item.getDescription() == null) {
            item.setDescription(items.get(item.getId()).getDescription());
        }

        if (item.getAvailable() == null) {
            item.setAvailable(items.get(item.getId()).getAvailable());
        }

        items.replace(item.getId(), item);

        Item updateItem = items.get(item.getId());

        return updateItem;
    }

    private long getId() {
        long maxUserId = items
            .values()
            .stream()
            .mapToLong(Item::getId)
            .max().orElse(0);
        return maxUserId + 1;
    }

    private void checkItem(Long itemId) {
        if (!items.containsKey(itemId)) {
            log.error("Item with id " + itemId + " not found");
            throw new NotFoundException("Item with id " + itemId + " not found");
        }
    }
}
