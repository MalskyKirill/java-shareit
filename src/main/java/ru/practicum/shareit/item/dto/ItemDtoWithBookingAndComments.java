package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoItem;
import ru.practicum.shareit.comment.dto.CommentDtoResponse;

import java.util.List;

@Data
public class ItemDtoWithBookingAndComments {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingDtoItem lastBooking;
    private BookingDtoItem nextBooking;
    private List<CommentDtoResponse> comments;
}
