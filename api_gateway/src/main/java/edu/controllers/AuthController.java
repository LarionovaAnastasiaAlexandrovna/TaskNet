package edu.controllers;

import dto.GeneraleResponseDTO;
import dto.LoginRequestDTO;
import dto.RegisterRequestDTO;
import dto.RegistrationResponseDTO;
import edu.email.EmailVerificationService;
import edu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final String INNER_URL = "http://localhost:8082/innerprosses/";
    private final RestTemplate restTemplate = new RestTemplate();  // Можно заменить на WebClient

    @Autowired JwtUtil jwtUtil;
    @Autowired EmailVerificationService emailVerificationService;

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(/*@Valid*/ @RequestBody RegisterRequestDTO request) {
        System.out.println("Запрос прилетает сюда");
        String scrapperUrl = INNER_URL + "auth/register";  // Адрес Scrapper-сервиса

        try {
            System.out.println("Запрос на регистрацию пытается отправиться");
            ResponseEntity<RegistrationResponseDTO> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request, getHeaders()),
                    RegistrationResponseDTO.class
            );

            System.out.println("Запрос на регистрацию отправился");

            emailVerificationService.sendVerificationEmail(request.getEmail());

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
        String scrapperUrl = INNER_URL + "auth/login";  // Адрес Scrapper-сервиса

        try {
            System.out.println("Запрос на регистрацию пытается отправиться");
            ResponseEntity<GeneraleResponseDTO> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request, getHeaders()),
                    GeneraleResponseDTO.class
            );

            System.out.println("Запрос на вход отправился");

            if (scrapperResponse.getStatusCode().is2xxSuccessful()) {
                String token = jwtUtil.generateTokenSession(request.getEmail());
                return ResponseEntity.ok(Collections.singletonMap("token", token));
            } else {
                return ResponseEntity.status(scrapperResponse.getStatusCode()).body(scrapperResponse.getBody());
            }
        } catch (Exception e) {
            System.out.println("Запрос на вход не отправился(");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка входа: " + e.getMessage());
        }
    }

    //@CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam(value = "token") String token) {
        System.out.println("Запрос прилетает на верификацию почты");
        String scrapperUrl = INNER_URL + "auth/verify";

        if (jwtUtil.isInvalidToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
        }

        String email = jwtUtil.extractEmail(token);
        System.out.println("Email из токена: " + email);

        try {
            System.out.println("Запрос на верификацию почты пытается отправиться");
            ResponseEntity<GeneraleResponseDTO> scrapperResponse = restTemplate.exchange(
                    scrapperUrl + "?email=" + email,
                    HttpMethod.GET,
                    new HttpEntity<>(getHeaders()),
                    GeneraleResponseDTO.class
            );

            System.out.println("Запрос на верификацию почты отправился");
            return ResponseEntity.status(scrapperResponse.getStatusCode()).body(scrapperResponse.getBody());

        } catch (Exception e) {
            System.out.println("Запрос на верификацию почты не отправился(");
//            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка верификации почты: " + e.getMessage());
        }
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(String email) {

        //TODO это написать потом
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
//
//        if (user.isEnabled()) {
//            return ResponseEntity.badRequest().body("Почта уже подтверждена");
//        }

        emailVerificationService.sendVerificationEmail(email);

        return ResponseEntity.ok("Письмо отправлено повторно");
    }
    
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
