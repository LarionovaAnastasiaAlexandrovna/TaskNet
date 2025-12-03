package edu.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ErrorResponse(int status, String message,
                                Instant timestamp, String path) {
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponse> handleWebClientException(
            WebClientResponseException ex,
            WebRequest request) {

        var error = new ErrorResponse(
                ex.getRawStatusCode(),
                extractErrorMessage(ex),
                Instant.now(),
                request.getDescription(false)
        );
        return ResponseEntity.status(ex.getStatusCode()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex,
            WebRequest request) {

        var error = new ErrorResponse(
                500,
                ex.getMessage(),
                Instant.now(),
                request.getDescription(false)
        );
        return ResponseEntity.internalServerError().body(error);
    }

    private String extractErrorMessage(WebClientResponseException ex) {
        try {
            return ex.getResponseBodyAsString();
        } catch (Exception e) {
            return ex.getMessage();
        }
    }
}
