package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.handler.ExceptionsHandler;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemRequestControllerTest {
    public static final String HEADER = "X-Sharer-User-Id";
    @Mock
    ItemRequestService itemRequestService;
    @InjectMocks
    ItemRequestController itemRequestController;
    private ObjectMapper objectMapper;
    private MockMvc mvc;
    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();
    private final User userOwner = User.builder()
            .name("owner")
            .email("owner@gmail.com")
            .build();

    private final ItemRequest itemRequest = ItemRequest.builder()
            .requestor(user)
            .created(LocalDateTime.now())
            .id(1L)
            .description("need item")
            .build();
    private final ItemRequestCreateDto itemRequestCreateDto = ItemRequestCreateDto.builder()
            .description("need item")
            .build();
    private final ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .id(1L)
            .requestorId(userOwner.getId())
            .description("need item")
            .created(itemRequest.getCreated())
            .requestorId(user.getId())
            .build();
    private final ItemRequestWithResponsesDto itemRequestWithResponsesDto = ItemRequestWithResponsesDto.builder()
            .id(1L)
            .requestorId(userOwner.getId())
            .description("need item")
            .created(itemRequest.getCreated())
            .requestorId(user.getId())
            .items(Collections.emptyList())
            .build();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemRequestController)
                .setControllerAdvice(new ExceptionsHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

    }

    @SneakyThrows
    @Test
    @Order(1)
    void return200WhenCreatingItemRequestAndItIsValid() {
        when(itemRequestService.createItemRequest(itemRequestCreateDto, user.getId())).thenReturn(itemRequestDto);

        String result = mvc.perform(post("/requests")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(itemRequestCreateDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestDto), result);
    }

    @SneakyThrows
    @Test
    @Order(2)
    void return400WhenCreatingItemRequestAndItIsInvalid() {
        itemRequestCreateDto.setDescription("");
        mvc.perform(post("/requests")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(itemRequestCreateDto)))
                .andExpect(status().isBadRequest());
        itemRequestCreateDto.setDescription("need item");
    }

    @SneakyThrows
    @Test
    @Order(3)
    void return200WhenGettingItemRequestById() {
        when(itemRequestService.returnItemRequestDtoById(1L, 1L)).thenReturn(itemRequestWithResponsesDto);

        String result = mvc.perform(get("/requests/1")
                        .header(HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemRequestWithResponsesDto), result);
    }

    @SneakyThrows
    @Test
    @Order(4)
    void return200WhenGettingAllItemRequests() {
        when(itemRequestService.returnItemRequestsByPage(0, 20, 1L)).thenReturn(List.of(itemRequestWithResponsesDto));

        String result = mvc.perform(get("/requests/all")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(20))
                        .header(HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemRequestWithResponsesDto)), result);
    }

    @SneakyThrows
    @Test
    @Order(5)
    void return200WhenGettingOwnItemRequests() {
        when(itemRequestService.returnAllItemRequestsByUserId(1L)).thenReturn(List.of(itemRequestWithResponsesDto));

        String result = mvc.perform(get("/requests")
                        .header(HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemRequestWithResponsesDto)), result);
    }
}
