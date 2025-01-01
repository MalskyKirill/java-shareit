package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.AlreadyExistsException;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = new UserServiceImpl(userRepository);
    private static User user;
    private static UserDto userDto;

    @BeforeAll
    static void setUp() {
        user = new User(1L, "name", "user1@mail.com");
        userDto = UserMapper.mapToUserDto(user);
    }

    @Test
    void getUserById() {
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(user));
        UserDto result = userService.getUser(1L);
        assertNotNull(result);
        assertEquals(result, UserMapper.mapToUserDto(user));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class)))
            .thenReturn(UserMapper.mapToUser(userDto));
        UserDto result = userService.createUser(userDto);
        assertNotNull(result);
        assertEquals(userDto, result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser() {
        UserDto updateUser = new UserDto(1L, "newName", "user1@mail.com");
        when(userRepository.findById(anyLong()))
            .thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class)))
            .thenReturn(UserMapper.mapToUser(updateUser));
        UserDto result = userService.updateUser(updateUser.getId(), updateUser);
        assertNotNull(result);
        assertEquals(updateUser, result);

        result.setEmail("new@mail.ru");
        when(userRepository.save(any(User.class)))
            .thenThrow(new AlreadyExistsException("Пользователь с такой почтой уже существует"));
        final AlreadyExistsException ex = Assertions.assertThrows(
            AlreadyExistsException.class,
            () -> userService.updateUser(result.getId(), result));
        Assertions.assertEquals("Пользователь с такой почтой уже существует", ex.getMessage());
        verify(userRepository, times(2)).save(any());
        verify(userRepository, times(2)).findById(anyLong());
    }

    @Test
    void deleteUserById() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

}
