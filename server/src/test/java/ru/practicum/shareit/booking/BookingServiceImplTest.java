package ru.practicum.shareit.booking;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private BookingServiceImpl bookingService;
    private final User user = User.builder()
            .id(1L)
            .name("user")
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
            .end(LocalDateTime.now().plusHours(10L))
            .start(LocalDateTime.now().plusHours(2L))
            .build();
    private final Booking booking = Booking.builder()
            .id(1L)
            .status(BookingStatus.WAITING)
            .start(LocalDateTime.now().plusHours(1L))
            .end(LocalDateTime.now().plusHours(10L))
            .booker(user)
            .item(item)
            .build();
    private final BookingDto currentBookingDto = BookingDto.builder()
            .id(1L)
            .status(BookingStatus.WAITING)
            .start(LocalDateTime.now().minusHours(1L))
            .end(LocalDateTime.now().plusHours(10L))
            .booker(user)
            .item(item)
            .build();
    private final BookingDto rejectedBookingDto = BookingDto.builder()
            .id(1L)
            .status(BookingStatus.REJECTED)
            .start(LocalDateTime.now().minusHours(1L))
            .end(LocalDateTime.now().plusHours(10L))
            .booker(user)
            .item(item)
            .build();
    private final BookingDto pastBookingDto = BookingDto.builder()
            .id(1L)
            .status(BookingStatus.WAITING)
            .start(LocalDateTime.now().minusHours(100L))
            .end(LocalDateTime.now().minusHours(90L))
            .booker(user)
            .item(item)
            .build();
    private final BookingDto futureBookingDto = BookingDto.builder()
            .id(1L)
            .status(BookingStatus.WAITING)
            .start(LocalDateTime.now().plusHours(100L))
            .end(LocalDateTime.now().plusHours(190L))
            .booker(user)
            .item(item)
            .build();

    @Order(1)
    @Test
    void testCreateBooking() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        assertThrows(IncorrectItemIdException.class, () -> bookingService.createBooking(user.getId(), bookingCreateDto));
        bookingCreateDto.setStart(bookingCreateDto.getStart().plusDays(100L));
        assertThrows(StartAfterEndException.class, () -> bookingService.createBooking(2L, bookingCreateDto));
        bookingCreateDto.setStart(bookingCreateDto.getStart().minusDays(100L));
        item.setAvailable(false);
        assertThrows(ItemUnavailableException.class, () -> bookingService.createBooking(2L, bookingCreateDto));
        item.setAvailable(true);
        assertEquals(bookingService.createBooking(2L, bookingCreateDto).getItem(), bookingDto.getItem());
        assertEquals(bookingService.createBooking(2L, bookingCreateDto).getId(), bookingDto.getId());
        assertEquals(bookingService.createBooking(2L, bookingCreateDto).getBooker(), bookingDto.getBooker());
    }

    @Order(2)
    @Test
    void testUpdateBooking() {
        lenient().when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
        lenient().when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
        lenient().when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        lenient().when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(booking));
        assertThrows(IncorrectUserIdException.class, () -> bookingService.updateBooking(10L, 1L, bookingUpdateDto));
        bookingUpdateDto.setStart(bookingUpdateDto.getStart().plusDays(100L));
        assertThrows(StartAfterEndException.class, () -> bookingService.updateBooking(1L, 1L, bookingUpdateDto));
        bookingUpdateDto.setStart(bookingUpdateDto.getStart().minusDays(100L));
        bookingUpdateDto.setItemId(2L);
        assertThrows(IncorrectItemIdException.class, () -> bookingService.updateBooking(1L, 1L, bookingUpdateDto));
        bookingUpdateDto.setItemId(1L);
        BookingDto updatedBooking = bookingService.updateBooking(1L, 1L, bookingUpdateDto);
        assertEquals(updatedBooking.getId(), booking.getId());
        assertEquals(updatedBooking.getId(), booking.getId());
        assertEquals(updatedBooking.getBooker(), booking.getBooker());
        assertEquals(updatedBooking.getItem(), booking.getItem());
        assertEquals(updatedBooking.getStart(), booking.getStart());
        assertEquals(updatedBooking.getEnd(), booking.getEnd());
    }

    @Order(3)
    @Test
    void testChangeBookingStatus() {
        lenient().when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        lenient().when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
        lenient().when(bookingRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));
        lenient().when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        assertEquals(bookingService.changeBookingStatus(1L, 1L, false).getStatus(), BookingStatus.REJECTED);
        assertEquals(bookingService.changeBookingStatus(1L, 1L, true).getStatus(), BookingStatus.APPROVED);
        assertThrows(BookingDoubleApproveException.class, () -> bookingService.changeBookingStatus(1L, 1L, true));
        assertThrows(BookingNotFoundException.class, () -> bookingService.changeBookingStatus(2L, 1L, true));
        assertThrows(NotFoundUserException.class, () -> bookingService.changeBookingStatus(2L, 2L, true));
    }

    @Order(5)
    @Test
    void testGetBookingById() {
        when(userRepository.existsById(1L)).thenReturn(true);
        lenient().when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        lenient().when(bookingRepository.findById(1L)).thenReturn(Optional.ofNullable(booking));
        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(2L, 1L));
        assertThrows(NotFoundUserException.class, () -> bookingService.getBookingById(1L, 2L));
        BookingDto newBooking = bookingService.getBookingById(1L, 1L);
        assertEquals(newBooking.getId(), booking.getId());
        assertEquals(newBooking.getId(), booking.getId());
        assertEquals(newBooking.getBooker(), booking.getBooker());
        assertEquals(newBooking.getItem(), booking.getItem());
        assertEquals(newBooking.getStart(), booking.getStart());
        assertEquals(newBooking.getEnd(), booking.getEnd());

    }

    @Order(6)
    @Test
    void testReturnAllBookingsByOwnerId() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        lenient().when(userRepository.existsById(1L)).thenReturn(true);
        when(bookingRepository.findByOwnerId(anyLong(), any(PageRequest.class))).thenReturn(List.of(bookingDto, currentBookingDto, futureBookingDto, pastBookingDto, rejectedBookingDto));
        when(bookingRepository.findByOwnerIdAndFutureState(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(futureBookingDto));
        when(bookingRepository.findByOwnerIdAndCurrentState(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(currentBookingDto));
        when(bookingRepository.findByOwnerIdAndPastState(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(pastBookingDto));
        when(bookingRepository.findByOwnerIdAndStatus(1L, BookingStatus.WAITING, pageRequest)).thenReturn(List.of(bookingDto));
        when(bookingRepository.findByOwnerIdAndStatus(1L, BookingStatus.REJECTED, pageRequest)).thenReturn(List.of(rejectedBookingDto));
        assertEquals(bookingService.returnAllBookingsByOwner(1L, "ALL", 0, 20).size(), 5);
        assertEquals(bookingService.returnAllBookingsByOwner(1L, "WAITING", 0, 20).get(0).getStatus(), BookingStatus.WAITING);
        assertTrue(bookingService.returnAllBookingsByOwner(1L, "CURRENT", 0, 20).get(0).getStart().isBefore(LocalDateTime.now()) &&
                bookingService.returnAllBookingsByOwner(1L, "CURRENT", 0, 20).get(0).getEnd().isAfter(LocalDateTime.now()));
        assertTrue(bookingService.returnAllBookingsByOwner(1L, "FUTURE", 0, 20).get(0).getStart().isAfter(LocalDateTime.now()) &&
                bookingService.returnAllBookingsByOwner(1L, "FUTURE", 0, 20).get(0).getEnd().isAfter(LocalDateTime.now()));
        assertTrue(bookingService.returnAllBookingsByOwner(1L, "PAST", 0, 20).get(0).getStart().isBefore(LocalDateTime.now()) &&
                bookingService.returnAllBookingsByOwner(1L, "PAST", 0, 20).get(0).getEnd().isBefore(LocalDateTime.now()));
        assertEquals(bookingService.returnAllBookingsByOwner(1L, "REJECTED", 0, 20).get(0).getStatus(), BookingStatus.REJECTED);
        assertThrows(UnsupportedBookingStateException.class, () -> bookingService.returnAllBookingsByOwner(1L, "UNKNOWN", 0, 20).size());
    }

    @Order(7)
    @Test
    void testReturnAllBookingsByBookerId() {
        PageRequest pageRequest = PageRequest.of(0, 20);
        lenient().when(userRepository.existsById(1L)).thenReturn(true);
        when(bookingRepository.findByBookerId(anyLong(), any(PageRequest.class))).thenReturn(List.of(bookingDto, currentBookingDto, futureBookingDto, pastBookingDto, rejectedBookingDto));
        when(bookingRepository.findByBookerIdAndFutureState(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(futureBookingDto));
        when(bookingRepository.findByBookerIdAndCurrentState(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(currentBookingDto));
        when(bookingRepository.findByBookerIdAndPastState(anyLong(), any(LocalDateTime.class), any(PageRequest.class))).thenReturn(List.of(pastBookingDto));
        when(bookingRepository.findByBookerIdAndStatus(1L, BookingStatus.WAITING, pageRequest)).thenReturn(List.of(bookingDto));
        when(bookingRepository.findByBookerIdAndStatus(1L, BookingStatus.REJECTED, pageRequest)).thenReturn(List.of(rejectedBookingDto));
        assertEquals(bookingService.returnAllBookings(1L, "ALL", 0, 20).size(), 5);
        assertEquals(bookingService.returnAllBookings(1L, "WAITING", 0, 20).get(0).getStatus(), BookingStatus.WAITING);
        assertTrue(bookingService.returnAllBookings(1L, "CURRENT", 0, 20).get(0).getStart().isBefore(LocalDateTime.now()) &&
                bookingService.returnAllBookings(1L, "CURRENT", 0, 20).get(0).getEnd().isAfter(LocalDateTime.now()));
        assertTrue(bookingService.returnAllBookings(1L, "FUTURE", 0, 20).get(0).getStart().isAfter(LocalDateTime.now()) &&
                bookingService.returnAllBookings(1L, "FUTURE", 0, 20).get(0).getEnd().isAfter(LocalDateTime.now()));
        assertTrue(bookingService.returnAllBookings(1L, "PAST", 0, 20).get(0).getStart().isBefore(LocalDateTime.now()) &&
                bookingService.returnAllBookings(1L, "PAST", 0, 20).get(0).getEnd().isBefore(LocalDateTime.now()));
        assertEquals(bookingService.returnAllBookings(1L, "REJECTED", 0, 20).get(0).getStatus(), BookingStatus.REJECTED);
        assertThrows(UnsupportedBookingStateException.class, () -> bookingService.returnAllBookings(1L, "UNKNOWN", 0, 20).size());
    }

}
