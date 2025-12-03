package dto;

public record ErrorResponseDTO(
        Integer errorCode,
        String errorMessage
) {
}