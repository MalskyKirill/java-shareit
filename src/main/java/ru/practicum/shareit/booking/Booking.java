package ru.practicum.shareit.booking;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {
    private Long id;
    @NotNull(message = "booking start can't be null")
    @PastOrPresent(message = "booking start can't be future")
    private LocalDateTime start;
    @NotNull(message = "booking end can't be null")
    private LocalDateTime end;
    @NotNull(message = "item can't be null")
    private Item item;
    @NotNull(message = "broker can't be null")
    private User broker;
    @NotNull(message = "booking status can't be null")
    private BookingStatus status;
}
