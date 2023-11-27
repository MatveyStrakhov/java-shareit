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

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class ExceptionHandler {
    @Data
    @Builder
    private static class ErrorJson {
        HttpStatus status;
        String error;
        LocalDateTime timestamp;
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = IncorrectEmailException.class)
    ResponseEntity<Object> handleIncorrectEmailException(IncorrectEmailException e) throws JsonProcessingException {
        ErrorJson error = ErrorJson.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT)
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = IncorrectUserIdException.class)
    ResponseEntity<Object> handleIncorrectUserIdException(IncorrectUserIdException e) throws JsonProcessingException {
        ErrorJson error = ErrorJson.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = IncorrectItemIdException.class)
    ResponseEntity<Object> handleIncorrectItemIdException(IncorrectItemIdException e) throws JsonProcessingException {
        ErrorJson error = ErrorJson.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = NotFoundUserException.class)
    ResponseEntity<Object> handleNotFoundUserException(NotFoundUserException e) throws JsonProcessingException {
        ErrorJson error = ErrorJson.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}