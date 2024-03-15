package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.handler.ExceptionsHandler;
import ru.practicum.shareit.item.comment.CommentCreateDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemControllerTest {
    public static final String HEADER = "X-Sharer-User-Id";
    @Mock
    ItemService itemService;
    @InjectMocks
    ItemController itemController;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;
    private final User user = User.builder()
            .id(1L)
            .name("user")
            .email("email@gmail.com")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("item")
            .description("description")
            .owner(user)
            .available(true)
            .requestId(1L)
            .build();
    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("item")
            .description("description")
            .ownerId(user.getId())
            .available(true)
            .requestId(1L)
            .comments(Collections.emptyList())
            .lastBooking(null)
            .nextBooking(null)
            .build();
    private final CommentCreateDto commentCreateDto = CommentCreateDto.builder()
            .text("comment")
            .build();
    private final CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .text("comment")
            .created(LocalDateTime.now())
            .authorName("user")
            .build();

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(itemController)
                .setControllerAdvice(new ExceptionsHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

    }

    @SneakyThrows
    @Order(1)
    @Test
    void return200WhenCreatingAndItemIsValid() {
        when(itemService.createItem(anyLong(), any(ItemDto.class))).thenReturn(itemDto);
        String result = mvc.perform(post("/items")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Test
    @Order(2)
    void return400WhenCreatingBookingAntNameIsInvalid() {
        itemDto.setName("");
        mvc.perform(post("/items")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());
        itemDto.setName("item");

    }

    @SneakyThrows
    @Test
    @Order(3)
    void return400WhenCreatingBookingAntDescriptionIsInvalid() {
        itemDto.setDescription("");
        mvc.perform(post("/items")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());
        itemDto.setDescription("item");

    }

    @SneakyThrows
    @Test
    @Order(4)
    void return400WhenCreatingBookingAntAvailableIsInvalid() {
        itemDto.setAvailable(null);
        mvc.perform(post("/items")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isBadRequest());
        itemDto.setAvailable(true);

    }

    @SneakyThrows
    @Order(5)
    @Test
    void return200WhenUpdatingAndItemIsValid() {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(itemDto);
        String result = mvc.perform(patch("/items/1")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);
    }

    @SneakyThrows
    @Order(6)
    @Test
    void return200WhenGettingAllItems() {
        when(itemService.returnAllItems(1L, 0, 20)).thenReturn(List.of(itemDto));
        String result = mvc.perform(get("/items")
                        .header(HEADER, 1L)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(20))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemDto)), result);
    }

    @SneakyThrows
    @Order(7)
    @Test
    void return200WhenSearchingItems() {
        when(itemService.searchItems("description", 0, 20)).thenReturn(List.of(itemDto));
        String result = mvc.perform(get("/items/search")
                        .header(HEADER, 1L)
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(20))
                        .param("text", "description")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(itemDto)), result);
    }

    @SneakyThrows
    @Order(8)
    @Test
    void return200WhenCreatingComment() {
        when(itemService.createComment(commentCreateDto, 1L, 1L)).thenReturn(commentDto);
        String result = mvc.perform(post("/items/1/comment")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(commentCreateDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }

    @SneakyThrows
    @Order(9)
    @Test
    void return200WhenCreatingInvalidComment() {
        commentCreateDto.setText("");
        mvc.perform(post("/items/1/comment")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(commentCreateDto)))
                .andExpect(status().isBadRequest());
        commentCreateDto.setText("description");
    }

    @SneakyThrows
    @Order(10)
    @Test
    void return200AndItemDtoWhenGettingItemById() {
        when(itemService.getItemById(1L,1L)).thenReturn(itemDto);
        String result = mvc.perform(get("/items/1")
                        .header(HEADER, 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(itemDto), result);

    }
}
