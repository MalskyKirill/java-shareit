package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserStorage {
    UserDto get(Long userId);
    UserDto create(Long userId);
    UserDto update(Long userId);
    UserDto delete(Long userId);
}
