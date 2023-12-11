package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        BookingDto bookingDto = BookingDto.builder()
                .id(booking.getId())
                .bookerId(booking.getBookerId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .itemId(booking.getItemId())
                .build();
        return bookingDto;
    }
}
