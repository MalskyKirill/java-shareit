package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemMapper {
    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequestId(item.getRequestId());
        return itemDto;
    }

    public static Item mapToItem(ItemDto itemDto, User user) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(user);
        item.setRequestId(itemDto.getRequestId());
        return item;
    }

    public static ItemDtoWithBookingAndComments mapToItemDtoWithBookingAndComments(Item item, BookingDtoItem nextBooking, BookingDtoItem lastBooking, List<CommentDtoResponse> comments) {
        ItemDtoWithBookingAndComments itemDtoWithBookingAndComments = new ItemDtoWithBookingAndComments();
        itemDtoWithBookingAndComments.setId(item.getId());
        itemDtoWithBookingAndComments.setName(item.getName());
        itemDtoWithBookingAndComments.setDescription(item.getDescription());
        itemDtoWithBookingAndComments.setAvailable(item.getAvailable());
        itemDtoWithBookingAndComments.setRequestId(item.getRequestId());
        itemDtoWithBookingAndComments.setNextBooking(nextBooking);
        itemDtoWithBookingAndComments.setLastBooking(lastBooking);
        itemDtoWithBookingAndComments.setComments(comments);
        return itemDtoWithBookingAndComments;
    }
}
