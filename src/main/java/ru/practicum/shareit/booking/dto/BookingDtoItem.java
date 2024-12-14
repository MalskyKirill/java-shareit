package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
public class BookingDtoItem {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private User broker;
    private BookingStatus status;
}
