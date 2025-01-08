package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.user.dto.UserDto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static ru.practicum.shareit.TestUtils.*;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationItemTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    public void getItemByIdTest() {
        UserDto savedUser = userService.createUser(owner);
        ItemDto savedItem = itemService.createItem(item2, savedUser.getId());
        UserDto savedRequester = userService.createUser(requester);
        bookingService.createBooking(bookingDtoRequest2, savedRequester.getId());
        ItemDtoWithBookingAndComments gotItem = itemService.getItem(savedUser.getId(), savedItem.getId());
        assertThat(gotItem.getId(), notNullValue());
        assertThat(gotItem.getName(), equalTo(savedItem.getName()));
        assertThat(gotItem.getAvailable(), equalTo(savedItem.getAvailable()));
        userService.deleteUser(savedUser.getId());
        userService.deleteUser(savedRequester.getId());

    }
}
