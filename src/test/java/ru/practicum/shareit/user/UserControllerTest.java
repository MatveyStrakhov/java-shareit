package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.handler.ExceptionsHandler;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {
    @Mock
    UserService userService;
    @InjectMocks
    UserController userController;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();
    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .email("email@email.com")
            .name("username")
            .build();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new ExceptionsHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

    }

    @SneakyThrows
    @Order(1)
    @Test
    void return200WhenCreatingAndUserIsValid() {
        when(userService.createUser(any(userDto.getClass()))).thenReturn(userDto);
        String response = mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(userDto), response);
    }

    @SneakyThrows
    @Order(2)
    @Test
    void return400WhenCreatingAndUserEmailIsInvalid() {
        userDto.setEmail(null);
        mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Order(3)
    @Test
    void return400WhenCreatingAndUserNameIsInvalid() {
        userDto.setName("");
        mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isBadRequest());
        userDto.setName("username");

    }

    @SneakyThrows
    @Order(4)
    @Test
    void return200WhenUpdatingAndUserIsValid() {
        when(userService.updateUser(anyLong(), any(userDto.getClass()))).thenReturn(userDto);
        String response = mvc.perform(patch("/users/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(userDto), response);
    }

    @SneakyThrows
    @Order(5)
    @Test
    void return200whenDeleteUser() {
        mvc.perform(delete("/users/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1)).deleteUser(1L);
    }

    @SneakyThrows
    @Order(6)
    @Test
    void return200WhenGettingUserById() {
        when(userService.getUserById(1L)).thenReturn(userDto);
        String response = mvc.perform(get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(userDto), response);
    }

    @SneakyThrows
    @Order(7)
    @Test
    void return400WhenGettingUserByInvalidId() {
        when(userService.getUserById(2L)).thenThrow(NotFoundUserException.class);
        mvc.perform(get("/users/2")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Order(8)
    @Test
    void return200WhenGettingAllUsers() {
        when(userService.returnAllUsers()).thenReturn(List.of(userDto));
        String response = mvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString(List.of(userDto)), response);
    }


}
