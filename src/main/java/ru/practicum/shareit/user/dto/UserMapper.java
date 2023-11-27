package ru.practicum.shareit.user.dto;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
        return userDto;
    }

    public static User toUser(UserDto userDto) {
        String email = userDto.getEmail();
        String name = userDto.getName();
        return User.builder()
                .name(name)
                .email(email)
                .build();
    }
}
