package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        Long id = userDto.getId();
        String email = userDto.getEmail();
        String name = userDto.getName();
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .build();
    }
}
