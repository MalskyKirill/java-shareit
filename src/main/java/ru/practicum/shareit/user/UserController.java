package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("GET-запрос к эндпоинту: '/users' на получение user с ID={}", userId);
        return userService.getUser(userId);
    }

    @PostMapping
    public UserDto createNewUser(@Valid @RequestBody UserDto userDto) {
        log.info("POST-запрос к эндпоинту: '/users' на добавление user");
        return userService.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("PATCH-запрос к эндпоинту: '/users' на обновление user с ID={}", userId);
        return userService.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable Long userId) {
        log.info("DELETE-запрос к эндпоинту: '/users' на удаление user с ID={}", userId);
        userService.deleteUser(userId);
    }
}
