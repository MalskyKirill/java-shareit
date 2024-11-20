package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
public class ItemDto {
    private Long id;
    @NotBlank(message = "item name can't be null, empty or blank")
    private String name;
    @NotBlank(message = "description can't be null, empty or blank")
    private String description;
    private Boolean available;
    private ItemRequest request;
}
