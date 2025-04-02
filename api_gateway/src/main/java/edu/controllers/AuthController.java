package edu.controllers;

import dto.RegisterRequestDTO;
import dto.RegistrationResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RestTemplate restTemplate = new RestTemplate();  // Можно заменить на WebClient

    @PostMapping("/registration")
    public ResponseEntity<?> registerUser(/*@Valid*/ @RequestBody RegisterRequestDTO request) {
        System.out.println("Запрос прилетает сюда");
        String scrapperUrl = "http://localhost:8082/innerprosses/auth/register";  // Адрес Scrapper-сервиса

        try {
            System.out.println("Запрос на регистрацию пытается отправиться");
            ResponseEntity<RegistrationResponse> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request, getHeaders()),
                    RegistrationResponse.class
            );

            System.out.println("Запрос на регистрацию отправился");
            return ResponseEntity.status(scrapperResponse.getStatusCode()).body(scrapperResponse.getBody());
        } catch (Exception e) {
            System.out.println("Запрос на регистрацию не отправился(");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка регистрации: " + e.getMessage());
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}



//package edu.controllers;
//
//import dto.LoginRequestDTO;
//import dto.RegisterRequestDTO;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Mono;
//
//
//@RestController
//@RequestMapping("localhost:8081/auth")
//@RequiredArgsConstructor //?
//public class AuthController {
//
//    private final WebClient webClient;
//
//    public AuthController(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
//    }
//
//    @PostMapping("/register")
//    public Mono<ResponseEntity<String/*MyResponseDTO.class если мне нужен объект, а не строка в ответе*/>> register(@RequestBody @Validated RegisterRequestDTO request) {
//        request.setPassword(hashPassword(request.getPassword())); // Хешируем пароль перед отправкой
//
//        return webClient.post()
//                .uri("/auth/register")
//                .bodyValue(request)
//                .retrieve()
//                .toEntity(String/*MyResponseDTO.class если мне нужен объект, а не строка в ответе*/.class);
//    }
//
//    @PostMapping("/login")
//    public Mono<ResponseEntity<String>> login(@RequestBody @Validated LoginRequestDTO request) {
//        return webClient.post()
//                .uri("/auth/login")
//                .bodyValue(request)
//                .retrieve()
//                .toEntity(String.class);
//    }
//
//    private String hashPassword(String password) {
//        return org.springframework.security.crypto.bcrypt.BCrypt.hashpw(password, org.springframework.security.crypto.bcrypt.BCrypt.gensalt());
//    }
//}
//
