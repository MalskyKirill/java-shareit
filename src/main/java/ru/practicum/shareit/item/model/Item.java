package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.*;
import lombok.Data;


import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

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
    private Boolean available;
    @NotNull(message = "owner can't' null")
    private User owner;
    private ItemRequest request;
}
