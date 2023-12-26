package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingCreateDto {
    @NotNull
    @FutureOrPresent
    LocalDateTime start;
    @NotNull
    @Future
    LocalDateTime end;
    @NotNull
    Long itemId;
}
