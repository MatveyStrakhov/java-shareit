package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;

import java.util.List;

public interface BookingService {
    List<BookingDto> returnAllBookings(Long userId, String state, int from, int size);

    List<BookingDto> returnAllBookingsByOwner(Long userId, String state, int from, int size);

    BookingDto getBookingById(Long id, Long userId);

    BookingDto updateBooking(Long userId, Long bookingId, BookingUpdateDto booking);

    BookingDto createBooking(Long userId, BookingCreateDto booking);

    BookingDto changeBookingStatus(Long bookingId, Long userId, Boolean approve);
}
