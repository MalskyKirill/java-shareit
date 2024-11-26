package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */

@Data
public class Item {

    private Long id;
    @NotBlank(message = "item name can't be null, empty or blank")
    private String name;
    @NotBlank(message = "description can't be null, empty or blank")
    private String description;
    @NotNull(message = "available can't be null")
    private Boolean available;
    private Long ownerId;
    private ItemRequest request;
}
