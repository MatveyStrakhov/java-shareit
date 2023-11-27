package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {

    Collection<User> returnAllUsers();

    User getUserById(Long id);

    User updateUser(Long id, UserDto user);

    User createUser(User user);

    boolean isValidUser(Long id);

    void deleteUser(Long id);
}
