package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
@Slf4j
public class UserStorageInMemoryImpl implements UserStorage {
    private Map<Long, User> users = new HashMap<>();
    private Set<String> userEmailList = new HashSet<>();

    @Override
    public User get(Long userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new NotFoundException("User with id " + userId + " not found");
        }
        log.info("user с ID = {}, достали из HashMap", user.getId());
        return user;
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

        log.info("user с ID = {}, добавлен в HashMap", user.getId());
        return user;
    }

    @Override
    public User update(User user) {
        checkUser(user.getId());

        if (user.getEmail() != null && !user.getEmail().equals(users.get(user.getId()).getEmail())) {
            checkEmail(user);
        }

        if (user.getName() == null) {
            user.setName(users.get(user.getId()).getName());
        }

        if (user.getEmail() == null) {
            user.setEmail(users.get(user.getId()).getEmail());
        }

        users.replace(user.getId(), user);
        User newUser =  users.get(user.getId());

        log.info("user с ID = {}, добавлен в HashMap", user.getId());
        return newUser;
    }

    @Override
    public void delete(Long userId) {
        checkUser(userId);
        userEmailList.remove(users.get(userId).getEmail());
        users.remove(userId);
        log.info("user с ID = {}, удален из HashMap", userId);
    }

    @Override
    public void checkUser(Long userId) {
        if (!users.containsKey(userId)) {
            log.error("User with id " + userId + " not found");
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
            log.error("User with e-mail " + user.getEmail() + " already created");
            throw new AlreadyExistsException("User with e-mail " + user.getEmail() + " already created");
        }
    }
}
