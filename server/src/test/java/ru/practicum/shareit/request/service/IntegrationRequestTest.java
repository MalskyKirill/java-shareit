package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoResp;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static ru.practicum.shareit.TestUtils.request;
import static ru.practicum.shareit.TestUtils.requester;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationRequestTest {
    private final RequestService requestService;
    private final UserService userService;

    @Test
    public void getRequestByIdTest() {
        UserDto savedRequester = userService.createUser(requester);
        ItemRequestDto shortRequestDto = new ItemRequestDto("description");
        ItemRequestDtoResp savedRequest = requestService.createRequest(shortRequestDto, savedRequester.getId());
        ItemRequestDtoResp gottenRequest = requestService.getRequestById(savedRequester.getId(), savedRequest.getId());
        assertThat(gottenRequest.getId(), notNullValue());
        assertThat(gottenRequest.getDescription(), equalTo(request.getDescription()));
        assertThat(gottenRequest.getCreated(), equalTo(savedRequest.getCreated()));
        assertThat(gottenRequest.getItems(), equalTo(Collections.emptyList()));
    }
}
