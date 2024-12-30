package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("POST-запрос к эндпоинту: '/users' на добавление user");
        return userClient.createUser(userRequestDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        log.info("GET-запрос к эндпоинту: '/users' на получение user с ID={}", userId);
        return userClient.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody UserRequestDto userRequestDto) {
        log.info("PATCH-запрос к эндпоинту: '/users' на обновление user с ID={}", userId);
        return userClient.updateUser(userId, userRequestDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        log.info("DELETE-запрос к эндпоинту: '/users' на удаление user с ID={}", userId);
        userClient.deleteUser(userId);
    }
}
