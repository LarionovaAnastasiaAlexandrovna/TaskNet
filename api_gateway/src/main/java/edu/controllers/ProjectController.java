package edu.controllers;

import dto.EmailRequestDTO;
import dto.ProjectDTO;
import dto.UserInProjectDTO;
import edu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private JwtUtil jwtUtil;

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestHeader("Authorization") String authHeader,
                                /*@Valid*/ @RequestBody ProjectDTO request) {
        try {
            System.out.println("Запрос прилетает на создание проекта");
            String scrapperUrl = "http://localhost:8082/innerprosses/project/create";  // Адрес Scrapper-сервиса

            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
            }

            String email = jwtUtil.extractEmail(token);
            System.out.println("Email из токена: " + email);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-User-Email", email);

            ResponseEntity<ProjectDTO> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    ProjectDTO.class
            );

            System.out.println("Запрос на создание проекта отправлен");
            return ResponseEntity.status(scrapperResponse.getStatusCode())
                    .body(scrapperResponse.getBody());

        } catch (Exception e) {
            System.out.println("Запрос на создание проекта не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка создания проекта: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/all")
    public ResponseEntity<?> getAllProject(@RequestHeader("Authorization") String authHeader) {

        String scrapperUrl = "http://localhost:8082/innerprosses/project/all";  // Адрес Scrapper-сервиса

        try {
        String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
        }

        String email = jwtUtil.extractEmail(token);
        System.out.println("Email из токена: " + email);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-User-Email", email);

            ResponseEntity<List<ProjectDTO>> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<List<ProjectDTO>>() {}
            );


        return ResponseEntity.status(scrapperResponse.getStatusCode())
                .body(scrapperResponse.getBody());

        } catch (Exception e) {
            System.out.println("Запрос на получение всех связанных проектов не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка получения всех связанных проектов: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/all-users")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> getAllProjectUsers(@PathVariable Long id,
                                                @RequestHeader("Authorization") String authHeader) {
        System.out.println("Запрос прилетает: получение данных о связанных пользователях");

        String scrapperUrl = "http://localhost:8082/innerprosses/project/" + id + "/all-users";

        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
            }

            ResponseEntity<List<UserInProjectDTO>> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(id),
                    new ParameterizedTypeReference<List<UserInProjectDTO>>() {}
            );

            System.out.println("Запрос на получение данных о связанных пользователях отправлен");
            return ResponseEntity.status(scrapperResponse.getStatusCode()).body(scrapperResponse.getBody());

        } catch (Exception e) {
            System.out.println("Запрос на получение данных о связанных пользователях не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка получения данных о связанных пользователях: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/add-user")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> addUserInProject(@PathVariable Long id,
                                              @RequestBody Map<String, String> requestBody,
                                              @RequestHeader("Authorization") String authHeader) {
        String scrapperUrl = "http://localhost:8082/innerprosses/project/" + id + "/add-user";
        String email = requestBody.get("email");

        System.out.println("Добавление пользователя с email: " + email + " в проект ID: " + id);

        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
            }

            EmailRequestDTO emailRequestDTO = new EmailRequestDTO(email);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<EmailRequestDTO> entity = new HttpEntity<>(emailRequestDTO, headers);

            ResponseEntity<Void> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.POST,
                    entity,
                    Void.class
            );

            System.out.println("Запрос на добавление пользователя с email: " + email + " в проект ID: " + id + " отправлен");
            return ResponseEntity.status(scrapperResponse.getStatusCode()).body(scrapperResponse.getBody());

        } catch (Exception e) {
            System.out.println("Запрос на добавление пользователя с email: " + email + " в проект ID: " + id + " не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка добавления пользователя с email: " + email + " в проект ID: " + id + " " + e.getMessage());
        }
    }
}
