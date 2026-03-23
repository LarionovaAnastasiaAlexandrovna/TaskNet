package edu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponse> handleWebClientException(
            WebClientResponseException ex) {

        try {
            ErrorResponse originalError = objectMapper.readValue(
                    ex.getResponseBodyAsString(),
                    ErrorResponse.class
            );

            log.debug("Propagating error from internal service: {}", originalError);
            return ResponseEntity.status(ex.getStatusCode()).body(originalError);

        } catch (Exception e) {
            log.warn("Failed to parse error response from internal service", e);

            ErrorResponse fallbackError = new ErrorResponse(
                    ex.getRawStatusCode(),
                    "Internal service error: " + ex.getMessage(),
                    LocalDateTime.now()
            );
            return ResponseEntity.status(ex.getStatusCode()).body(fallbackError);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        log.error("Unexpected error", ex);

        ErrorResponse error = new ErrorResponse(
                500,
                "Internal server error: " + ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity.internalServerError().body(error);
    }
}