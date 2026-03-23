package edu.controllers;

import dto.user.LoginRequestDTO;
import dto.user.RegisterRequestDTO;
import dto.user.RegisterResponseDTO;
//import edu.email.EmailVerificationService;
import edu.service.ScrapperAuthClient;
import edu.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final ScrapperAuthClient scrapperAuthClient;
    private final JwtUtil jwtUtil;
//    private final EmailVerificationService emailVerificationService;

    @PostMapping("/registration")
    public ResponseEntity<RegisterResponseDTO> registerUser(@RequestBody RegisterRequestDTO request) {
        log.info("Получен запрос на регистрацию: {}", request.getEmail());
        var response = scrapperAuthClient.register(request);
        //emailVerificationService.sendVerificationEmail(request.getEmail());
        //log.info("Email отправлен пользователю: {}", request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody LoginRequestDTO request) {
        log.info("Попытка входа {}", request.getEmail());
        scrapperAuthClient.login(request);
        String token = jwtUtil.generateTokenSession(request.getEmail());
        log.info("Пользователь успешно вошёл: {}", request.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        if (jwtUtil.isInvalidToken(token)) {
            log.warn("Неверный токен при верификации");
            return ResponseEntity.status(401).body("Недействительный токен");
        }

        String email = jwtUtil.extractEmail(token);
        log.info("Верификация email: {}", email);

        var response = scrapperAuthClient.verifyEmail(email);
        return ResponseEntity.ok(response);
    }
}
