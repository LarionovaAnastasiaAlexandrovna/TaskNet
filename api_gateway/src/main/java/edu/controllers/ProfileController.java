package edu.controllers;

import dto.user.UserDTO;
import edu.service.ScrapperProfileClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ScrapperProfileClient scrapperProfileClient;

    @GetMapping
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        log.info("Запрос на отображение профиля пользователя");

        try {
            String email = authentication.getName();
            log.debug("Email из токена: {}", email);

            var response = scrapperProfileClient.getUserProfile(email);
            log.info("Профиль успешно получен для пользователя: {}", email);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Ошибка при получении профиля", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка отображения профиля: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserDTO userDTO) {
        log.info("Запрос на обновление профиля пользователя: {}", userDTO.getEmail());

        try {
            var response = scrapperProfileClient.updateUserProfile(userDTO);
            log.info("Профиль успешно обновлен для пользователя: {}", userDTO.getEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Ошибка при обновлении профиля", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка обновления профиля: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> deleteUserProfile(@RequestBody UserDTO userDTO) {
        log.info("Запрос на обновление профиля пользователя: {}", userDTO.getEmail());

        try {
            var response = scrapperProfileClient.updateUserProfile(userDTO);
            log.info("Профиль успешно обновлен для пользователя: {}", userDTO.getEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Ошибка при обновлении профиля", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка обновления профиля: " + e.getMessage());
        }
    }
}