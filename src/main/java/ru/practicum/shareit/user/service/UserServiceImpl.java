package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto getUser(Long userId) {
        UserDto userDto = UserMapper.mapToUserDto(userStorage.get(userId));
        log.info("получен user с ID = {}", userDto.getId());
        return userDto;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        UserDto newUserDto = UserMapper.mapToUserDto(userStorage.create(user));
        log.info("создан новый user с ID = {}", newUserDto.getId());
        return newUserDto;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        userDto.setId(userId);
        User user = UserMapper.mapToUser(userDto);
        UserDto updateUserDto = UserMapper.mapToUserDto(userStorage.update(user));
        log.info("обновлен user с ID = {}", userId);
        return updateUserDto;
    }

    @Override
    public void deleteUser(Long userId) {
        userStorage.delete(userId);
        log.info("удален user с ID = {}", userId);
    }
}
