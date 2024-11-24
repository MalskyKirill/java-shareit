package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ItemStorageInMemoryImpl implements ItemStorage{
    private Map<Long, Item> items = new HashMap<>();

    @Override
    public Item create(Item item) {
        if (item.getId() != null && items.containsKey(item.getId())) {
            throw new AlreadyExistsException("User with id " + item.getId() + " already created");
        };

        item.setId(getId());
        items.put(item.getId(), item);
        return item;
    }

    private long getId() {
        long maxUserId = items
            .values()
            .stream()
            .mapToLong(Item::getId)
            .max().orElse(0);
        return maxUserId + 1;
    }
}
