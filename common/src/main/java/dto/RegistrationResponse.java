package dto;

import lombok.Data;

@Data
public class RegistrationResponse {
    private String message;
    private String token;  // Если Scrapper возвращает токен
}

