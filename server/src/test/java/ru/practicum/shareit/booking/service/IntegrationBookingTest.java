package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.enums.BookingStatus;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static ru.practicum.shareit.TestUtils.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationBookingTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    public void getBookingByIdTest() {
        UserDto savedUser = userService.createUser(owner);
        UserDto savedBooker = userService.createUser(booker);
        ItemDto savedItem = itemService.createItem(item, savedUser.getId());
        User userO = UserMapper.mapToUser(savedUser);
        Item itemO = ItemMapper.mapToItem(savedItem, userO);
        BookingDto bookingDto = bookingService.createBooking(bookingDtoRequest, savedBooker.getId());
        BookingDto gotBooking = bookingService.getBooking(savedBooker.getId(), bookingDto.getId());
        assertThat(gotBooking.getId(), notNullValue());
        assertThat(gotBooking.getItem(), equalTo(itemO));
        assertThat(gotBooking.getBooker(), equalTo(UserMapper.mapToUser(savedBooker)));
        assertThat(gotBooking.getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(gotBooking.getStart(), equalTo(bookingDtoRequest.getStart()));
        assertThat(gotBooking.getEnd(), equalTo(bookingDtoRequest.getEnd()));
        userService.deleteUser(savedUser.getId());
        userService.deleteUser(savedBooker.getId());
    }

    @Test
    public void exceptionTest() {
        UserDto savedUser = userService.createUser(owner);
        UserDto savedBooker = userService.createUser(booker);
        BookingDtoRequest bookingReq = bookingDtoRequest;

        try {
            bookingService.createBooking(bookingDtoRequest, savedBooker.getId());
        } catch (NotFoundException e) {
            assertThat(e.getMessage(), equalTo("Item with id 1 not found"));
        }

        ItemDto savedItem = itemService.createItem(item, savedUser.getId());

        bookingReq.setItemId(savedItem.getId());
        try {
            bookingService.createBooking(bookingDtoRequest, savedUser.getId());
        } catch (ValidationException e) {
            assertThat(e.getMessage(), equalTo("The user cannot book an item belonging to him"));
        }

        savedItem.setAvailable(false);
        itemService.updateItem(savedUser.getId(), savedItem.getId(), savedItem);
        try {
            bookingService.createBooking(bookingDtoRequest, savedBooker.getId());
        } catch (ValidationException e) {
            assertThat(e.getMessage(), equalTo("Item with id " + savedItem.getId() + " not available"));
        }

        userService.deleteUser(savedUser.getId());
        userService.deleteUser(savedBooker.getId());
    }
}
