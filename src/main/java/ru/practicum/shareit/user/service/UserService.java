package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto getUserById(Long userId);

    UserDto createUser(Long userId);
    UserDto updateUserById(Long userId);
    void deleteUserById(Long userId);
}
