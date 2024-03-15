package ru.practicum.shareit.user;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import ru.practicum.shareit.exception.IncorrectEmailException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;

    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@gmail.com")
            .build();
    private final User userIncorrectEmail = User.builder()
            .id(1L)
            .name("username2")
            .email("bedlam")
            .build();
    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("username")
            .email("email@gmail.com")
            .build();
    private final UserDto userIncorrectEmailDto = UserDto.builder()
            .id(1L)
            .name("username2")
            .email("bedlam")
            .build();

    @Order(1)
    @Test
    void testCreateUser() {
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.save(userIncorrectEmail)).thenThrow(DataIntegrityViolationException.class);
        assertEquals(userService.createUser(userDto), userDto);
        assertThrows(IncorrectEmailException.class, () -> userService.createUser(userIncorrectEmailDto));
    }

    @Order(2)
    @Test
    void testUpdateUser() {
        lenient().when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertEquals(userService.updateUser(1L, userDto), userDto);
        assertThrows(NotFoundUserException.class, () -> userService.updateUser(2L, userDto));
    }

    @Order(3)
    @Test
    void testReturnAllUsers() {
        when(userRepository.findAllAndReturnDto()).thenReturn(List.of(userDto));
        assertEquals(userService.returnAllUsers(), List.of(userDto));

    }

    @Order(4)
    @Test
    void testIsValidUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(10L)).thenReturn(false);
        assertTrue(userService.isValidUser(1L));
        assertFalse(userService.isValidUser(10L));
    }

    @Order(5)
    @Test
    void testGetUserById() {
        lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        lenient().when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertEquals(userService.getUserById(1L).getId(), userDto.getId());
        assertThrows(NotFoundUserException.class, () -> userService.getUserById(2L));
    }

    @Order(6)
    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);
        Mockito.verify(userRepository).deleteById(1L);
    }
}
