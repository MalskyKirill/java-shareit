package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.handler.ErrorResponse;
import ru.practicum.shareit.handler.ValidationErrorResponse;
import ru.practicum.shareit.handler.Violation;

import java.util.List;

@SpringBootTest
class ShareItTests {

    @Test
    void contextLoads() {
        new ErrorResponse("I", "d");
        new ValidationErrorResponse(List.of());
        new Violation("i", "d");
    }

}
