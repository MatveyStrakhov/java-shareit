package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.IncorrectEmailException;
import ru.practicum.shareit.exception.IncorrectItemIdException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserStorageImpl implements UserStorage {
    private Long userId = 1L;
    Map<Long, User> users = new HashMap<>();

    @Override
    public List<UserDto> returnAllUsers() {
        return users.values().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.toUserDto(users.get(id));
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        User previous = users.get(id);
        user.setId(id);
        if (users.values().stream().map(User::getEmail)
                .anyMatch(email -> email.equals(user.getEmail()))
                && !previous.getEmail().equals(user.getEmail())) {
            throw new IncorrectEmailException("Email already exists!");
        }
        if (user.getName() == null) {
            user.setName(previous.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(previous.getEmail());
        }
        users.put(id, user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (users.values().stream().map(User::getEmail)
                .noneMatch(email -> email.equals(userDto.getEmail()))) {
            userDto.setId(getId());
            User user = UserMapper.toUser(userDto);
            users.put(user.getId(), user);
            return userDto;
        } else throw new IncorrectEmailException("Email already exists!");
    }

    @Override
    public boolean isValidUser(Long id) {
        return users.containsKey(id);
    }

    @Override
    public void deleteUser(Long id) {
        if (isValidUser(id)) {
            users.remove(id);
        } else {
            throw new IncorrectItemIdException("Incorrect Item Id!");
        }
    }

    private Long getId() {
        return userId++;
    }
}
