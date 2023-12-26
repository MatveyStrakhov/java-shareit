package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping()
    public List<BookingDto> getAllBookings(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.returnAllBookings(userId);
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(@NotNull @PathVariable Long id, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingById(id, userId);
    }

    @PatchMapping("/{id}")
    public BookingDto updateBooking(@RequestParam(required = false) Boolean approved, @Valid @RequestBody(required = false) BookingUpdateDto booking, @PathVariable("id") Long bookingId, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        if (approved != null) {
            return bookingService.changeBookingStatus(bookingId, userId, approved);
        } else {
            return bookingService.updateBooking(userId, bookingId, booking);
        }
    }

    @PostMapping()
    public BookingDto addBooking(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid @RequestBody BookingCreateDto booking) {
        return bookingService.createBooking(userId, booking);
    }
}
