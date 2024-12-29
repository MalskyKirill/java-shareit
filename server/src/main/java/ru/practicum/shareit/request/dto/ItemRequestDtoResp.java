package ru.practicum.shareit.request.dto;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ItemRequestDtoResp {
    private Long id;
    private String description;
    private UserDto requestor;
    private LocalDateTime created;
    private List<Item> items;
}
