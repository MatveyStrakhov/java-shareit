package ru.practicum.shareit.item;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.IncorrectItemIdException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentCreateDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemServiceImplTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    ItemServiceImpl itemService;
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
    private final UserDto userDto = UserDto.builder()
            .id(1L)
            .email("email@gmail.com")
            .name("user")
            .build();
    private final CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .text("comment")
            .created(LocalDateTime.now())
            .authorName("user")
            .build();
    private final ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("item")
            .description("description")
            .available(true)
            .requestId(1L)
            .ownerId(1L)
            .comments(List.of(commentDto))
            .lastBooking(null)
            .nextBooking(null)
            .build();
    private final CommentCreateDto commentCreateDto = CommentCreateDto.builder()
            .text("comment")
            .build();
    private final Comment comment =  Comment.builder()
            .id(1L)
            .text("comment")
            .created(LocalDateTime.now())
            .itemId(1L)
            .userId(1L)
            .build();


    @Order(1)
    @Test
    void testCreateItem() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenThrow(NotFoundUserException.class);
        when(commentRepository.findCommentsByItemId(1L)).thenReturn(List.of(commentDto));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        assertEquals(itemService.createItem(1L, itemDto), itemDto);
        assertThrows(NotFoundUserException.class, () -> itemService.createItem(2L, itemDto));
    }

    @Order(2)
    @Test
    void testUpdateItem() {
        lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        lenient().when(userRepository.findById(2L)).thenThrow(NotFoundUserException.class);
        lenient().when(commentRepository.findCommentsByItemId(1L)).thenReturn(List.of(commentDto));
        lenient().when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findById(2L)).thenThrow(IncorrectItemIdException.class);
        assertEquals(itemService.updateItem(1L, 1L, itemDto), itemDto);
        assertThrows(NotFoundUserException.class, () -> itemService.updateItem(2L, 1L, itemDto));
        assertThrows(IncorrectItemIdException.class, () -> itemService.updateItem(1L, 2L, itemDto));

    }

    @Order(3)
    @Test
    void testSearchItems() {
        Page<ItemDto> page = new PageImpl<>(List.of(itemDto));
        when(itemRepository.findByNameAndDescription("description", PageRequest.of(0, 20)))
                .thenReturn(page);
        assertEquals(itemService.searchItems("description", 0, 20), List.of(itemDto));
        assertEquals(itemService.searchItems("", 0, 20), Collections.emptyList());
    }

    @Order(4)
    @Test
    void testReturnAllItems() {
        Page<ItemDto> page = new PageImpl<>(List.of(itemDto));
        when(itemRepository.findByOwnerId(1L, PageRequest.of(0, 20))).thenReturn(page);
        lenient().when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        assertEquals(itemService.returnAllItems(1L, 0, 20).size(), 1);
    }

    @Order(5)
    @Test
    void testGetItemById() {
        lenient().when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        lenient().when(itemRepository.findById(2L)).thenThrow(IncorrectItemIdException.class);
        assertEquals(itemService.getItemById(1L, 1L).getId(), itemDto.getId());
        assertThrows(IncorrectItemIdException.class, () -> itemService.getItemById(2L, 1L));
    }

    @Order(6)
    @Test
    void testCreateComment() {
        lenient().when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookingRepository.isBooker(anyLong(),anyLong(),any(LocalDateTime.class))).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        assertEquals(itemService.createComment(commentCreateDto,1L,1L).getAuthorName(),commentDto.getAuthorName());

    }
}
