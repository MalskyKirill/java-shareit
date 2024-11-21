package ru.practicum.shareit.user.storage;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserStorageInMemoryImpl implements UserStorage{
    private Map<Long, User> users = new HashMap<>();

    @Override
    public UserDto get(Long userId) {
        return UserMapper.mapToUserDto(users.get(userId));
    }

    @Override
    public UserDto create(Long userId) {
        return null;
    }

    @Override
    public UserDto update(Long userId) {
        return null;
    }

    @Override
    public UserDto delete(Long userId) {
        return null;
    }

    private long getId() {
        long maxUserId = users.values().stream().mapToLong(User::getId).max().orElse(0);
        return maxUserId + 1;
    }
}
