package ru.practicum.shareit.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
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

    @ExceptionHandler(value = {IllegalArgumentException.class})
    ResponseEntity<Object> handleIllegalArgumentException(RuntimeException e) throws JsonProcessingException {
        ErrorJson error = ErrorJson.builder()
                .error(e.getMessage())
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
