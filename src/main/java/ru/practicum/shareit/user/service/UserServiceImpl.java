package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserStorage userStorage;

    @Override
    public UserDto getUser(Long userId) {
        return userStorage.get(userId);
    }

    @Override
    public UserDto createUser(Long userId) {
        return null;
    }

    @Override
    public UserDto updateUser(Long userId) {
        return null;
    }

    @Override
    public void deleteUser(Long userId) {

    }
}
