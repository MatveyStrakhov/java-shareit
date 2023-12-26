package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectEmailException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> returnAllUsers() {
        return userRepository.findBy();
    }

    @Override
    public UserDto getUserById(Long id) {
        return UserMapper.toUserDto(userRepository.findById(id).orElseThrow(() -> new NotFoundUserException("User not found!")));
    }

    @Override
    public UserDto updateUser(Long id, UserDto user) {
        User previous = userRepository.findById(id).orElseThrow(() -> new NotFoundUserException("User not found!"));
        user.setId(id);
        if (user.getName() == null) {
            user.setName(previous.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(previous.getEmail());
        }
        User newUser = UserMapper.toUser(user);
        try {
            return UserMapper.toUserDto(userRepository.save(newUser));
        } catch (DataIntegrityViolationException e) {
            throw new IncorrectEmailException("Email already in use!");
        }
    }

    @Override
    public UserDto createUser(UserDto user) {
        User newUser = UserMapper.toUser(user);
        try {
            return UserMapper.toUserDto(userRepository.save(newUser));
        } catch (DataIntegrityViolationException e) {
            throw new IncorrectEmailException("Email already in use!");
        }
    }

    @Override
    public boolean isValidUser(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
