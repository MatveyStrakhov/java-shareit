package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingUpdateDto;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Objects;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@Validated
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @GetMapping()
    public List<BookingDto> getAllBookings(@RequestParam(required = false) String state, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                           @Valid @PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                           @Valid @Positive @RequestParam(required = false, value = "size", defaultValue = "1000") Integer size) {
        return bookingService.returnAllBookings(userId, Objects.requireNonNullElse(state, "ALL"), from, size);
    }

    @GetMapping("/{id}")
    public BookingDto getBookingById(@NotNull @PathVariable Long id, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return bookingService.getBookingById(id, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingByOwnerId(@RequestParam(required = false) String state, @NotNull @RequestHeader("X-Sharer-User-Id") Long userId, @Valid @PositiveOrZero @RequestParam(required = false, value = "from", defaultValue = "0") Integer from,
                                                @Valid @Positive @RequestParam(required = false, value = "size", defaultValue = "1000") Integer size) {
        return bookingService.returnAllBookingsByOwner(userId, Objects.requireNonNullElse(state, "ALL"), from, size);
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
