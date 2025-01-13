package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * TODO Sprint add-item-requests.
 */

@Data
public class ItemRequestDto {
    @NotNull(message = "request description name can't be null")
    private String description;
}
