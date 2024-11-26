package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto getUser(Long userId) {
        return UserMapper.mapToUserDto(userStorage.get(userId));
    }

    @Override
    public UserDto createUser(User user) {
        return UserMapper.mapToUserDto(userStorage.create(user));
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        user.setId(userId);
        return UserMapper.mapToUserDto(userStorage.update(user));
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.delete(userId);
    }
}
