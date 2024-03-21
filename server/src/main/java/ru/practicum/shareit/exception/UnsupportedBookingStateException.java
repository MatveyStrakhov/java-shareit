package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedBookingStateException extends RuntimeException {
    public UnsupportedBookingStateException(String message) {
        super(message);
    }
}
