package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class ItemDto {
    private Long id;
    @NotBlank(message = "item name can't be null, empty or blank")
    private String name;
    @NotBlank(message = "description can't be null, empty or blank")
    private String description;
    @NotNull(message = "available can't be null")
    private Boolean available;
    private Long requestId;
}
