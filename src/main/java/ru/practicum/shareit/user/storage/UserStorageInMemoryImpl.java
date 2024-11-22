package ru.practicum.shareit.user.storage;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class UserStorageInMemoryImpl implements UserStorage{
    private Map<Long, User> users = new HashMap<>();
    private Set<String> userEmailList = new HashSet<>();

    @Override
    public User get(Long userId) {
        System.out.println(users.get(userId));
        return users.get(userId);
    }

    @Override
    public User create(User user) {
        if (userEmailList.contains(user.getEmail())) {
            throw new AlreadyExistsException("Пользователь с e-mail " + user.getEmail() + "уже создан");
        }
        userEmailList.add(user.getEmail());

        if (user.getId() != null) {
            throw new AlreadyExistsException("Пользователь с id " + user.getId() + "уже создан");
        }

        user.setId(getId());
        users.put(user.getId(), user);
        return user;
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
