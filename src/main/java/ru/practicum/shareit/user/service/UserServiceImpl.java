package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getUser(Long userId) {
        UserDto userDto = UserMapper.mapToUserDto(userRepository.getReferenceById(userId));
        log.info("получен user с ID = {}", userDto.getId());
        return userDto;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        UserDto newUserDto = UserMapper.mapToUserDto(userRepository.save(user));
        log.info("создан новый user с ID = {}", newUserDto.getId());
        return newUserDto;
    }

    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        userDto.setId(userId);
        User user = UserMapper.mapToUser(userDto);
        UserDto updateUserDto = UserMapper.mapToUserDto(userRepository.save(user));
        log.info("обновлен user с ID = {}", userId);
        return updateUserDto;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("удален user с ID = {}", userId);
    }
}
