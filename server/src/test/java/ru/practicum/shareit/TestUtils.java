package ru.practicum.shareit;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

public class TestUtils {
    public static UserDto owner = new UserDto(1L, "owner", "owner@mail.com");

    public static ItemDto item = new ItemDto(1L, "item", "description", true, 1L);
}
