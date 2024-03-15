package ru.practicum.shareit.request;

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
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.NotFoundUserException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithResponsesDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemRequestServiceImplTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    ItemRequestServiceImpl itemRequestService;
    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@gmail.com")
            .build();
    private final User userOwner = User.builder()
            .id(3L)
            .name("owner")
            .email("owner@gmail.com")
            .build();
    private final Item item = Item.builder()
            .id(1L)
            .name("item")
            .description("description")
            .owner(userOwner)
            .available(true)
            .requestId(1L)
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
            .description("need item")
            .created(itemRequest.getCreated())
            .requestorId(user.getId())
            .items(Collections.emptyList())
            .build();

    @Order(1)
    @Test
    void testCreateItemRequest() {
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertEquals(itemRequestService.createItemRequest(itemRequestCreateDto, 1L), itemRequestDto);
        assertThrows(NotFoundUserException.class, () -> itemRequestService.createItemRequest(itemRequestCreateDto, 2L));
    }

    @Order(2)
    @Test
    void testReturnItemRequestDtoById() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.existsById(2L)).thenReturn(false);
        when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
        when(itemRequestRepository.findById(2L)).thenReturn(Optional.empty());
        when(itemRepository.findByRequestId(1L)).thenReturn(Collections.emptyList());
        assertEquals(itemRequestService.returnItemRequestDtoById(1L, 1L), itemRequestWithResponsesDto);
        assertThrows(NotFoundUserException.class, () -> itemRequestService.returnItemRequestDtoById(2L, 1L));
        assertThrows(ItemRequestNotFoundException.class, () -> itemRequestService.returnItemRequestDtoById(1L, 2L));
    }

    @Order(3)
    @Test
    void testReturnItemRequestsByPage() {
        Page<ItemRequest> page = new PageImpl<>(List.of(itemRequest));
        when(itemRequestRepository.findAll(any(PageRequest.class))).thenReturn(page);
        assertEquals(itemRequestService.returnItemRequestsByPage(0, 20, 1L), Collections.emptyList());
        assertEquals(itemRequestService.returnItemRequestsByPage(0, 20, 3L), List.of(itemRequestWithResponsesDto));
    }

    @Order(4)
    @Test
    void testReturnAllItemRequestsByUserId() {
        lenient().when(userRepository.existsById(1L)).thenReturn(true);
        lenient().when(userRepository.existsById(2L)).thenReturn(false);
        lenient().when(itemRequestRepository.findByRequestorId(1L)).thenReturn(List.of(itemRequestWithResponsesDto));
        lenient().when(itemRequestRepository.findByRequestorId(2L)).thenReturn(Collections.emptyList());
        assertEquals(itemRequestService.returnAllItemRequestsByUserId(1L), List.of(itemRequestWithResponsesDto));
        assertThrows(NotFoundUserException.class, () -> itemRequestService.returnAllItemRequestsByUserId(2L));
    }

    @Order(5)
    @Test
    void testReturnAllItemRequests() {
        Page<ItemRequest> page = new PageImpl<>(List.of(itemRequest));
        lenient().when(itemRequestRepository.findAll()).thenReturn(List.of(itemRequest));
        when(itemRepository.findByRequestId(1L)).thenReturn(Collections.emptyList());
        assertEquals(itemRequestService.returnAllItemRequests(),List.of(itemRequestWithResponsesDto));
    }
}
