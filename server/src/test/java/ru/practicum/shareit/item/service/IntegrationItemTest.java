package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBookingAndComments;
import ru.practicum.shareit.user.dto.UserDto;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.service.UserService;

import static ru.practicum.shareit.TestUtils.item;
import static ru.practicum.shareit.TestUtils.owner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;


@Transactional
@SpringBootTest(properties = "jdbc.url=jdbc:postgresql://localhost:5432/test", webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationItemTest {
    private final ItemService itemService;
    private final UserService userService;

    @Test
    public void getItemByIdTest() {
        UserDto savedUser = userService.createUser(owner);
        ItemDto savedItem = itemService.createItem(item, savedUser.getId());
        ItemDtoWithBookingAndComments gotItem = itemService.getItem(savedUser.getId(), savedItem.getId());
        assertThat(gotItem.getId(), notNullValue());
        assertThat(gotItem.getName(), equalTo(savedItem.getName()));
        assertThat(gotItem.getAvailable(), equalTo(savedItem.getAvailable()));
        userService.deleteUser(savedUser.getId());
    }
}
