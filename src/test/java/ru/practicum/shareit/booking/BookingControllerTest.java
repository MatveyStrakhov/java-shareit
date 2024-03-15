package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.exception.UnsupportedBookingStateException;
import ru.practicum.shareit.handler.ExceptionsHandler;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingControllerTest {
    public static final String HEADER = "X-Sharer-User-Id";
    @Mock
    BookingService bookingService;
    @InjectMocks
    BookingController bookingController;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    private final User user = User.builder()
            .id(1L)
            .name("username")
            .email("email@email.com")
            .build();

    private final Item item = Item.builder()
            .id(1L)
            .name("item")
            .description("description")
            .owner(user)
            .available(true)
            .requestId(1L)
            .build();

    private final BookingCreateDto bookingCreateDto = BookingCreateDto.builder()
            .itemId(1L)
            .start(LocalDateTime.now().plusHours(1L))
            .end(LocalDateTime.now().plusHours(10L))
            .build();
    private final BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .status(BookingStatus.WAITING)
            .start(LocalDateTime.now().plusHours(1L))
            .end(LocalDateTime.now().plusHours(10L))
            .booker(user)
            .item(item)
            .build();
    private final BookingUpdateDto bookingUpdateDto = BookingUpdateDto.builder()
            .itemId(1L)
            .end(LocalDateTime.now().plusHours(100L))
            .start(LocalDateTime.now().plusHours(1L))
            .build();


    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(bookingController)
                .setControllerAdvice(new ExceptionsHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

    }

    @SneakyThrows
    @Test
    @Order(1)
    void return200WhenCreatingBookingAndItIsValid() {
        when(bookingService.createBooking(user.getId(), bookingCreateDto)).thenReturn(bookingDto);

        String result = mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(bookingCreateDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    @Order(2)
    void return400WhenCreatingBookingAntItIsInvalid() {
        bookingCreateDto.setStart(bookingCreateDto.getStart().minusDays(1L));
        mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(bookingCreateDto)))
                .andExpect(status().isBadRequest());

    }

    @SneakyThrows
    @Test
    @Order(3)
    void return200WhenUpdatingBookingAndItIsValid() {
        when(bookingService.updateBooking(user.getId(), 1L, bookingUpdateDto)).thenReturn(bookingDto);

        String result = mvc.perform(patch("/bookings/1")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(bookingUpdateDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    @Order(4)
    void return400WhenUpdatingBookingAndItIsValid() {
        bookingUpdateDto.setStart(bookingUpdateDto.getStart().minusDays(1L));
        mvc.perform(patch("/bookings/1")
                        .contentType("application/json")
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(bookingUpdateDto)))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    @Order(5)
    void return200WhenGettingBookingById() {
        lenient().when(bookingService.getBookingById(1L, 1L)).thenReturn(bookingDto);

        String result = mvc.perform(get("/bookings/1")
                        .header(HEADER, user.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    @Order(6)
    void return200WhenGettingBookingsByOwnerId() {
        when(bookingService.returnAllBookingsByOwner(user.getId(), "All", 0, 20)).thenReturn((List.of(bookingDto)));

        String result = mvc.perform(get("/bookings/owner")
                        .param("state", "All")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(20))
                        .header(HEADER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString((List.of(bookingDto))), result);
    }

    @SneakyThrows
    @Test
    @Order(7)
    void return200WhenGettingAllBookings() {
        when(bookingService.returnAllBookings(user.getId(), "All", 0, 20)).thenReturn((List.of(bookingDto)));

        String result = mvc.perform(get("/bookings")
                        .param("state", "All")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(20))
                        .header(HEADER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(objectMapper.writeValueAsString((List.of(bookingDto))), result);
    }

    @SneakyThrows
    @Test
    @Order(8)
    void return400WhenGettingAllBookings() {
        when(bookingService.returnAllBookings(user.getId(), "UNSUPPORTED", 0, 20)).thenThrow(UnsupportedBookingStateException.class);

        mvc.perform(get("/bookings")
                        .param("state", "UNSUPPORTED")
                        .param("from", String.valueOf(0))
                        .param("size", String.valueOf(20))
                        .header(HEADER, user.getId()))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    @Order(3)
    void return200WhenChangingBookingStatus() {
        when(bookingService.changeBookingStatus(1L, 1L, true)).thenReturn(bookingDto);

        String result = mvc.perform(patch("/bookings/1")
                        .contentType("application/json")
                        .param("approved", String.valueOf(true))
                        .header(HEADER, user.getId())
                        .content(objectMapper.writeValueAsString(bookingUpdateDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

}
