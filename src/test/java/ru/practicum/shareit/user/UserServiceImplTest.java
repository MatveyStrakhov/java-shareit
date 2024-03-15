package ru.practicum.shareit.user;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

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
    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .name("username")
            .email("email@gmail.com")
            .build();

    @Order(1)
    @Test
    void testCreateUser() {
        when(userRepository.save(user)).thenReturn(user);
        assertEquals(userService.createUser(userDto), userDto);
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
        assertEquals(userService.returnAllUsers(),List.of(userDto));

    }
}
