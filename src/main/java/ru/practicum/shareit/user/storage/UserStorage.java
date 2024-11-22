package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserStorage {
    User get(Long userId);
    User create(User user);
    UserDto update(Long userId);
    UserDto delete(Long userId);
}
