package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public Collection<User> returnAllUsers() {
        return userStorage.returnAllUsers();
    }

    @Override
    public User getUserById(Long id) {
        return userStorage.getUserById(id);
    }

    @Override
    public User updateUser(Long id, UserDto user) {
        return userStorage.updateUser(id, user);
    }

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public boolean isValidUser(Long id) {
        return userStorage.isValidUser(id);
    }

    @Override
    public void deleteUser(Long id) {
        userStorage.deleteUser(id);
    }
}
