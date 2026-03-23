package edu.controllers;

import dto.ErrorResponse;
import exception.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException ex) {
        HttpStatus status = switch (ex.getType()) {
            case USER_ALREADY_EXISTS -> HttpStatus.CONFLICT;  // 409
            case USER_NOT_FOUND -> HttpStatus.NOT_FOUND;      // 404
            case INVALID_PASSWORD -> HttpStatus.UNAUTHORIZED; // 401
        };

        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(errorResponse);
    }
}