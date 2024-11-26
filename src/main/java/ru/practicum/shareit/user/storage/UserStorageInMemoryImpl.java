package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class UserStorageInMemoryImpl implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private Set<String> userEmailList = new HashSet<>();

    @Override
    public User get(Long userId) {
        checkUser(userId);
        return users.get(userId);
    }

    @Override
    public User create(User user) {
        checkEmail(user);
        userEmailList.add(user.getEmail());

        if (user.getId() != null && users.containsKey(user.getId())) {
            throw new AlreadyExistsException("User with id " + user.getId() + " already created");
        }

        user.setId(getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        checkUser(user.getId());
        checkEmail(user);

        if (user.getName() == null) {
            user.setName(users.get(user.getId()).getName());
        }

        if (user.getEmail() == null) {
            user.setEmail(users.get(user.getId()).getEmail());
        }

        users.replace(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void delete(Long userId) {
        checkUser(userId);
        userEmailList.remove(users.get(userId).getEmail());
        users.remove(userId);
    }

    @Override
    public void checkUser(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
    }

    private long getId() {
        long maxUserId = users
            .values()
            .stream()
            .mapToLong(User::getId)
            .max().orElse(0);
        return maxUserId + 1;
    }

    private void checkEmail(User user) {
        if (userEmailList.contains(user.getEmail())) {
            throw new AlreadyExistsException("User with e-mail " + user.getEmail() + " already created");
        }
    }
}
