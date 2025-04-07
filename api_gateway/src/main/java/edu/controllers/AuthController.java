package edu.controllers;

import dto.GeneraleResponseDTO;
import dto.LoginRequestDTO;
import dto.RegisterRequestDTO;
import dto.RegistrationResponseDTO;
import edu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RestTemplate restTemplate = new RestTemplate();  // Можно заменить на WebClient

    @Autowired
    JwtUtil jwtUtil;

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(/*@Valid*/ @RequestBody RegisterRequestDTO request) {
        System.out.println("Запрос прилетает сюда");
        String scrapperUrl = "http://localhost:8082/innerprosses/auth/register";  // Адрес Scrapper-сервиса

        try {
            System.out.println("Запрос на регистрацию пытается отправиться");
            ResponseEntity<RegistrationResponseDTO> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request, getHeaders()),
                    RegistrationResponseDTO.class
            );

            System.out.println("Запрос на регистрацию отправился");
            return ResponseEntity.status(scrapperResponse.getStatusCode()).body(scrapperResponse.getBody());
        } catch (Exception e) {
            System.out.println("Запрос на регистрацию не отправился(");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка регистрации: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequestDTO request) {
        System.out.println("Запрос прилетает на вход");
        String scrapperUrl = "http://localhost:8082/innerprosses/auth/login";  // Адрес Scrapper-сервиса

        try {
            System.out.println("Запрос на регистрацию пытается отправиться");
            ResponseEntity<GeneraleResponseDTO> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request, getHeaders()),
                    GeneraleResponseDTO.class
            );

            System.out.println("Запрос на вход отправился");

            // Если Scrapper-сервис вернул успешный статус (2xx), создаём токен
            if (scrapperResponse.getStatusCode().is2xxSuccessful()) {
                String token = jwtUtil.generateToken(request.getEmail());
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            } else {
                // Если Scrapper-сервис вернул ошибку, возвращаем её клиенту
                return ResponseEntity.status(scrapperResponse.getStatusCode()).body(scrapperResponse.getBody());
            }
        } catch (Exception e) {
            System.out.println("Запрос на вход не отправился(");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка входа: " + e.getMessage());
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
