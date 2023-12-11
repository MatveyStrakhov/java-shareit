package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

   List<UserDto> returnAllUsers();

    UserDto getUserById(Long id);

    UserDto updateUser(Long id, UserDto user);

    UserDto createUser(UserDto user);

    boolean isValidUser(Long id);

    void deleteUser(Long id);
}
