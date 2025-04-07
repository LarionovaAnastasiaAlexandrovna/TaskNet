package edu.controllers;

import dto.ProfileResponseDTO;
import dto.UserDTO;
import edu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        System.out.println("Запрос прилетает: отображение профиля");

        String scrapperUrl = "http://localhost:8082/innerprosses/profile";

        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
            }

            String email = jwtUtil.extractEmail(token);
            System.out.println("Email из токена: " + email);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-User-Email", email); // Передаём email через заголовок (например)

            ResponseEntity<ProfileResponseDTO> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    ProfileResponseDTO.class
            );

            System.out.println("Запрос на отображение профиля отправлен");
            return ResponseEntity.status(scrapperResponse.getStatusCode()).body(scrapperResponse.getBody());

        } catch (Exception e) {
            System.out.println("Запрос на отображение профиля не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка отображения профиля: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/update")
    public ResponseEntity<?> updateUserProfile(@RequestHeader("Authorization") String authHeader,
                                               @RequestBody UserDTO userDTO) {
        System.out.println("Запрос прилетает: обновление профиля");

        String scrapperUrl = "http://localhost:8082/innerprosses/profile/update";

        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
            }

            HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDTO);

            ResponseEntity<UserDTO> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.PUT,
                    requestEntity,
                    UserDTO.class
            );

            System.out.println("Запрос на обновление профиля отправлен");
            return ResponseEntity.status(scrapperResponse.getStatusCode()).body(scrapperResponse.getBody());

        } catch (Exception e) {
            System.out.println("Запрос на обновление профиля не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка обновления профиля: " + e.getMessage());
        }
    }
}
