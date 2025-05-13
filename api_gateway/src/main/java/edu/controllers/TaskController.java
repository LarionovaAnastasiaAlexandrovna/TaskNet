package edu.controllers;

import dto.TaskDTO;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private JwtUtil jwtUtil;

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestHeader("Authorization") String authHeader,
            /*@Valid*/ @RequestBody TaskDTO request) {
        try {
            System.out.println("Запрос прилетает на создание задачи");
            String scrapperUrl = "http://localhost:8082/innerprosses/task/create";  // Адрес Scrapper-сервиса

            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
            }

            ResponseEntity<TaskDTO> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request),
                    TaskDTO.class
            );

            System.out.println("Запрос на создание задачи отправлен");
            return ResponseEntity.status(scrapperResponse.getStatusCode())
                    .body(scrapperResponse.getBody());

        } catch (Exception e) {
            System.out.println("Запрос на создание задачи не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка создания задачи: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/recent")
    public ResponseEntity<?> getRecentTasks(@RequestHeader("Authorization") String authHeader) {
        System.out.println("Запрос прилетает: получение недавних задач");

        String scrapperUrl = "http://localhost:8082/innerprosses/task/recent";

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

            ResponseEntity<List<TaskDTO>> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<List<TaskDTO>>() {}
            );

            System.out.println("Запрос на получение недавних задач отправлен");
            return ResponseEntity.status(scrapperResponse.getStatusCode()).body(scrapperResponse.getBody());

        } catch (Exception e) {
            System.out.println("Запрос на получение недавних задач не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка получения недавних задач: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PatchMapping("/{id}/view")
    public ResponseEntity<?> updateLastView(@PathVariable Long id,
                                            @RequestHeader("Authorization") String authHeader) {

        System.out.println("Запрос прилетает: обновление последнего просмотра задачи");
        String scrapperUrl = "http://localhost:8082/innerprosses/task/" + id + "/view";

        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
            }

            String email = jwtUtil.extractEmail(token);
            System.out.println("Email из токена: " + email);

            HttpEntity<Void> emptyRequest = new HttpEntity<>(null);

            // Выполняем PATCH-запрос без тела
            // пока не рабочий
            // TODO переделать под WebClient
            ResponseEntity<Void> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.PATCH,
                    emptyRequest,
                    Void.class
            );

            System.out.println("Запрос на обновление последнего просмотра задачи отправлен");
            return ResponseEntity.status(scrapperResponse.getStatusCode())
                    .body(scrapperResponse.getBody());

        } catch (Exception e) {
            System.out.println("Запрос на обновление последнего просмотра задачи не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка создания задачи: " + e.getMessage());
        }
    }
}
