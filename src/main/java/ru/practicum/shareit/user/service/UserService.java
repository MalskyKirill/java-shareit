package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {

    UserDto getUser(Long userId);

    UserDto createUser(User user);
    UserDto updateUser(Long userId, User user);
    void deleteUser(Long userId);
}
