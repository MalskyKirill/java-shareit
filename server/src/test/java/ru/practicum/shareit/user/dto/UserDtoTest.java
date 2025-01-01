package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDtoTest {
    private final JacksonTester<UserDto> json;
    private UserDto userDto;

    @BeforeEach
    void beforeEach() {
        userDto = new UserDto(1L, "Kirill", "kirill@shareit.ru");
    }

    @Test
    void testSerialize() throws Exception {

        JsonContent<UserDto> result = json.write(userDto);

        assertThat(result).hasJsonPath("$.id")
            .hasJsonPath("$.name")
            .hasJsonPath("$.email");

        assertThat(result).extractingJsonPathNumberValue("$.id")
            .satisfies(id -> assertThat(id.longValue()).isEqualTo(userDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.name")
            .satisfies(name -> assertThat(name).isEqualTo(userDto.getName()));
        assertThat(result).extractingJsonPathStringValue("$.email")
            .satisfies(email -> assertThat(email).isEqualTo(userDto.getEmail()));
    }

}
