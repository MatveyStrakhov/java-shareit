package ru.practicum.shareit.booking;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class Booking {
    long id;
    LocalDateTime start;
    LocalDateTime end;
    long itemId;
    long bookerId;
    BookingStatus status;

}
