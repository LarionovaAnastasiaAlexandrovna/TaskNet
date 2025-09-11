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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String INNER_URL = "http://localhost:8082/innerprosses/";

    @Autowired private JwtUtil jwtUtil;

    @GetMapping
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        System.out.println("Запрос прилетает: отображение профиля");

        String scrapperUrl = INNER_URL + "profile";

        try {
            String email = authentication.getName(); // Email из токена
            System.out.println("Email из токена: " + email);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-User-Email", email);

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
    public ResponseEntity<?> updateUserProfile(@RequestBody UserDTO userDTO) {
        System.out.println("Запрос прилетает: обновление профиля");

        String scrapperUrl = INNER_URL + "profile/update";

        try {
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
