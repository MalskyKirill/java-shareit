package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDto getUser(Long userId) {
        UserDto userDto = UserMapper.mapToUserDto(userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User with id " + userId + " not found")));
        log.info("получен user с ID = {}", userDto.getId());
        return userDto;
    }

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        try {
            User user = UserMapper.mapToUser(userDto);
            UserDto newUserDto = UserMapper.mapToUserDto(userRepository.save(user));
            log.info("создан новый user с ID = {}", newUserDto.getId());
            return newUserDto;
        } catch (DataIntegrityViolationException ex) {
            log.error("User with e-mail " + userDto.getEmail() + " already created");
            throw new AlreadyExistsException("User with e-mail " + userDto.getEmail() + " already created");
        }
    }

    @Transactional
    @Override
    public UserDto updateUser(Long userId, UserDto userDto) {
        userDto.setId(userId);

        User updateUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User with id " + userId + " not found"));

        if (userDto.getName() != null) {
            updateUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null && !userDto.getEmail().equals(updateUser.getEmail())) {
            List<User> matchEmailUsers = userRepository.findUsersByEmail(userDto.getEmail())
                .stream()
                .filter(user -> user.getEmail().equals(userDto.getEmail()))
                .toList();

            if(matchEmailUsers.isEmpty()) {
                updateUser.setEmail(userDto.getEmail());
            } else {
                log.error("User with e-mail " + userDto.getEmail() + " already created");
                throw new AlreadyExistsException("User with e-mail " + userDto.getEmail() + " already created");
            }
        }

        UserDto updateUserDto = UserMapper.mapToUserDto(userRepository.save(updateUser));
        log.info("обновлен user с ID = {}", userId);
        return updateUserDto;
    }

    @Transactional
    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("удален user с ID = {}", userId);
    }
}
