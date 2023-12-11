package ru.practicum.shareit.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.practicum.shareit.exception.IncorrectEmailException;
import ru.practicum.shareit.exception.IncorrectItemIdException;
import ru.practicum.shareit.exception.IncorrectUserIdException;
import ru.practicum.shareit.exception.NotFoundUserException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class ExceptionsHandler {
    @Data
    @Builder
    private static class ErrorJson {
        HttpStatus status;
        String error;
        LocalDateTime timestamp;
    }

    @ExceptionHandler(value = IncorrectEmailException.class)
    ResponseEntity<Object> handleIncorrectEmailException(IncorrectEmailException e) throws JsonProcessingException {
        ErrorJson error = ErrorJson.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT)
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = IncorrectUserIdException.class)
    ResponseEntity<Object> handleIncorrectUserIdException(IncorrectUserIdException e) throws JsonProcessingException {
        ErrorJson error = ErrorJson.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {IncorrectItemIdException.class, NotFoundUserException.class})
    ResponseEntity<Object> handleIncorrectItemIdExceptionAndNotFoundUserException(RuntimeException e) throws JsonProcessingException {
        ErrorJson error = ErrorJson.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}