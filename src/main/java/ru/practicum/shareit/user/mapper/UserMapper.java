package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User mapToUser(UserDto userDto) {
        User newUser = new User();
        newUser.setId(userDto.getId());
        newUser.setName(userDto.getName());
        newUser.setEmail(userDto.getEmail());
        return newUser;
    }
}
