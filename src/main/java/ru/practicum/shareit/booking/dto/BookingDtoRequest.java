package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookingDtoRequest {
    @NotNull(message = "itemId name can't be null")
    private Long itemId;
    @NotNull(message = "start data can't be null")
    @FutureOrPresent
    private LocalDateTime start;
    @NotNull(message = "end data can't be null")
    @Future
    private LocalDateTime end;
}
