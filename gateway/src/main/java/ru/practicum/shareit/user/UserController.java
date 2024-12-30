package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserRequestDto;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserRequestDto userRequestDto) {
        return userClient.createUser(userRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers() {
        return userClient.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        return userClient.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId,
                                             @RequestBody UserRequestDto userRequestDto) {
        return userClient.updateUser(userId, userRequestDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userClient.deleteUser(userId);
    }
}
