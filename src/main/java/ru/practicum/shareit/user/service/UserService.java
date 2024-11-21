package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {

    UserDto getUser(Long userId);

    UserDto createUser(Long userId);
    UserDto updateUser(Long userId);
    void deleteUser(Long userId);
}
