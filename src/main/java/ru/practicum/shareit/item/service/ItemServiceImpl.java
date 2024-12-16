package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final BookingService bookingService;
    private final CommentService commentService;

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
    public ItemDtoWithBookingAndComments getItem(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> {
            log.error("Item with id " + itemId + " not found");
            throw new NotFoundException("Item with id " + itemId + " not found");
        });

        List<CommentDtoResponse> comments = commentService.getAllCommentsByItemId(itemId);

        ItemDtoWithBookingAndComments itemDto = ItemMapper.mapToItemDtoWithBookingAndComments(item, null, null, comments);

        if (item.getOwner().getId().equals(userId)) { // если запрашивает владелец вещи
            List<BookingDtoItem> bookings = bookingService.getAllBookingsByItem(itemId);

            BookingDtoItem last = bookings.getLast();
            BookingDtoItem next = bookings.stream().filter(b -> b.getStart().isAfter(LocalDateTime.now())).findFirst().orElse(null);

            itemDto.setNextBooking(next);
            itemDto.setLastBooking(last);
        }

        log.info("получен item с ID = {}", item.getId());
        return itemDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDtoWithBookingAndComments> getAllItemsByUser(Long userId) {
        userService.getUser(userId);
        List<Item> items = itemRepository.findItemsByOwnerId(userId);
        if (items == null) {
            log.info("пользователя с ID = {} список вещей пуст", userId);
            return null;
        }

        List<Long> itemsId = items.stream().map(Item::getId).collect(Collectors.toList());

        Map<Long, List<BookingDtoItem>> bookingsDto = bookingService.getAllBookingsBySomeItems(itemsId);
        Map<Item, List<CommentDtoResponse>> commentsDto = commentService.getAllCommentsBySomeItems(itemsId);


        List<ItemDtoWithBookingAndComments> itemsDto = items.stream()
            .map(i -> {

                if (bookingsDto.get(i.getId()) == null) {
                    return ItemMapper.mapToItemDtoWithBookingAndComments(i, null, null, null);
                }

                BookingDtoItem next = bookingsDto.get(i.getId()).stream().filter(b -> b.getStart().isAfter(LocalDateTime.now())).findFirst().orElseThrow(null);
                BookingDtoItem last = bookingsDto.get(i.getId()).getLast();

                if (commentsDto.get(i) == null) {
                    return ItemMapper.mapToItemDtoWithBookingAndComments(i, next, last, null);
                }
                List<CommentDtoResponse> comments = commentsDto.get(i);

                return ItemMapper.mapToItemDtoWithBookingAndComments(i, next, last, comments);
            })
            .toList(); // бежим по коллекции item и мапим каждый в itemDto

        log.info("получен список item у пользователя с ID = {}", userId);
        return itemsDto;
    }

    @Transactional(readOnly = true)
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

    @Transactional
    @Override
    public ItemDto updateItem(Long userId, Long itemId, ItemDto itemDto) {
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
