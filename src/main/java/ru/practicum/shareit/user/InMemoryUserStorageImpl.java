package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.IncorrectEmailException;
import ru.practicum.shareit.exception.IncorrectItemIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.*;

@Repository
public class InMemoryUserStorageImpl implements UserStorage {
    private Long userId = 1L;
    Map<Long, User> users = new HashMap<>();
    Set<String> emails = new HashSet<>();


    private Long getId() {
        return userId++;
    }

    @Override
    public Collection<User> returnAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public User updateUser(Long id, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User previous = users.get(id);
        user.setId(id);
        if (emails.contains(user.getEmail()) && !previous.getEmail().equals(user.getEmail())) {
            throw new IncorrectEmailException("Email already exists!");
        }
        if (user.getName() == null) {
            user.setName(previous.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(previous.getEmail());
        } else {
            emails.remove(previous.getEmail());
            emails.add(user.getEmail());
        }
        users.put(id, user);
        return user;
    }

    @Override
    public User createUser(User user) {
        if (!emails.contains(user.getEmail())) {
            user.setId(getId());
            users.put(user.getId(), user);
            emails.add(user.getEmail());

            return user;
        } else throw new IncorrectEmailException("Email-name combination already exists!");
    }

    @Override
    public boolean isValidUser(Long id) {
        return users.containsKey(id);
    }

    @Override
    public void deleteUser(Long id) {
        if (isValidUser(id)) {
            emails.remove(users.get(id).getEmail());
            users.remove(id);
        } else {
            throw new IncorrectItemIdException("Incorrect Item Id!");
        }
    }
}
