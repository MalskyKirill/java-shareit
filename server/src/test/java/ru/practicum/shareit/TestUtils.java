package ru.practicum.shareit;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResp;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

public class TestUtils {
    public static UserDto owner = new UserDto(1L, "owner", "owner@mail.com");
    public static UserDto user = new UserDto(3L, "user", "user@mail.com");
    public static UserDto booker = new UserDto(5L, "booker", "booker@mail.com");
    public static UserDto requester = new UserDto(2L, "requester", "requester@mail.com");
    public static ItemDto item = new ItemDto(1L, "item", "description", true, 1L);
    public static ItemDto item2 = new ItemDto(2L, "item", "description", true, 1L);
    public static Item itemR = new Item(1L, "Item1", "Description1", true, UserMapper.mapToUser(requester), null);
    public static ItemRequestDtoResp request = new ItemRequestDtoResp(1L, "description", requester, LocalDateTime.now(), List.of(itemR));
    public static LocalDateTime now = LocalDateTime.of(2024, 12, 12, 10, 0, 0);
    public static BookingDtoRequest bookingDtoRequest = new BookingDtoRequest(1L, now, now.plusDays(8));
    public static BookingDtoRequest bookingDtoRequest2 = new BookingDtoRequest(3L, now, now.plusDays(8));
    public static ItemRequestDto requestDto = new ItemRequestDto("request");
}
