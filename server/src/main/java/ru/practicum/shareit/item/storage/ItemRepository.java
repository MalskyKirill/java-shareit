package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemsByOwnerId(Long ownerId);

    @Query("SELECT it FROM Item AS it WHERE (lower(it.name) LIKE lower(concat('%', :search, '%')) OR lower(it.description) LIKE lower(concat('%', :search, '%'))) AND it.available=true")
    List<Item> getItemsBySearchQuery(@Param("search") String text);

    List<Item> findAllByItemRequest(ItemRequest itemRequest);

    List<Item> findAllByItemRequestIn(java.util.List<ru.practicum.shareit.request.model.ItemRequest> requestsList);
}
