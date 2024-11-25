package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemStorageInMemoryImpl implements ItemStorage{
    private final Map<Long, Item> items = new HashMap<>();
    private final Map<Long, List<Item>> itemsByUser = new HashMap<>(); // создаем мапу для хранения item юзера

    @Override
    public Item create(Item item) {
        if (item.getId() != null && items.containsKey(item.getId())) {
            throw new AlreadyExistsException("User with id " + item.getId() + " already created");
        };

        item.setId(getId());
        items.put(item.getId(), item);

        if (!itemsByUser.containsKey(item.getOwnerId())) { // ежели в мапе itemsByUser нет ключа userId
            itemsByUser.put(item.getOwnerId(), new ArrayList<>()); // добавляем в мапу новые ключ-лист
        }

        List<Item> userItems = itemsByUser.get(item.getOwnerId()); // берем список item юзера
        userItems.add(item); // добавляем новую вещь
        itemsByUser.put(item.getOwnerId(), userItems); // обнавляем мапу

        return item;
    }

    @Override
    public Item get(Long itemId) {
        checkItem(itemId);
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItemsByUser(Long userId) {
        return itemsByUser.get(userId);
    }

    @Override
    public List<Item> getSearchItemList(String text) {

        return items.values() // берем значения
            .stream() // преобразуем в стрим
            .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())) // проверяем наличие подстроки в строке
            .filter(Item::getAvailable) // проверяем на доступность
            .collect(Collectors.toList()); // собираем в коллекцию
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
            throw new NotFoundException("Item with id " + itemId + " not found");
        }
    }
}
