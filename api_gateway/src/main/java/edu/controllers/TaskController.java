package edu.controllers;

import dto.CommentDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String INNER_URL = "http://localhost:8082/innerprosses/";

    @Autowired private JwtUtil jwtUtil;

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestHeader("Authorization") String authHeader,
            /*@Valid*/ @RequestBody TaskDTO request) {
        try {
            System.out.println("Запрос прилетает на создание задачи");
            String scrapperUrl = INNER_URL + "task/create";  // Адрес Scrapper-сервиса

            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (jwtUtil.isInvalidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
            }

            String email = jwtUtil.extractEmail(token);
            System.out.println("Email из токена: " + email);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("X-User-Email", email);

            ResponseEntity<TaskDTO> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
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

        String scrapperUrl = INNER_URL + "task/recent";

        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (jwtUtil.isInvalidToken(token)) {
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
                    new ParameterizedTypeReference<>() {}
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
    @PutMapping("/{id}/view")
    public ResponseEntity<?> updateLastView(@PathVariable Long id,
                                            @RequestHeader("Authorization") String authHeader) {

        System.out.println("Запрос прилетает: обновление последнего просмотра задачи");
        String scrapperUrl = INNER_URL + "task/" + id + "/view";

        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (jwtUtil.isInvalidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
            }

            String email = jwtUtil.extractEmail(token);
            System.out.println("Email из токена: " + email);

            HttpEntity<Void> emptyRequest = new HttpEntity<>(null);

            // TODO переделать под WebClient
            ResponseEntity<Void> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.PUT,
                    emptyRequest,
                    Void.class
            );

            System.out.println("Запрос на обновление последнего просмотра задачи отправлен");
            return ResponseEntity.status(scrapperResponse.getStatusCode())
                    .body(scrapperResponse.getStatusCode().is2xxSuccessful()
                            ? null
                            : "Ошибка при обновлении просмотра задачи");

        } catch (Exception e) {
            System.out.println("Запрос на обновление последнего просмотра задачи не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка создания задачи: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateTask(@PathVariable Long id,
                                        @RequestHeader("Authorization") String authHeader,
                                        @RequestBody TaskDTO taskDTO) {
        System.out.println("Запрос прилетает: обновление задачи");

        String scrapperUrl = INNER_URL + "task/" + id + "/update";

        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (jwtUtil.isInvalidToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
            }

            HttpEntity<TaskDTO> requestEntity = new HttpEntity<>(taskDTO);

            ResponseEntity<TaskDTO> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.PUT,
                    requestEntity,
                    TaskDTO.class
            );

            System.out.println("Запрос на обновление задачи отправлен");
            return ResponseEntity.status(scrapperResponse.getStatusCode()).body(scrapperResponse.getBody());

        } catch (Exception e) {
            System.out.println("Запрос на обновление задачи не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка обновления задачи: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/{id}/add-comment")
    public ResponseEntity<?> addComment(@PathVariable Long id,
                                        @RequestHeader("Authorization") String authHeader,
                                        @RequestBody CommentDTO request) {

        System.out.println("Запрос прилетает: добавление нового комментария к задаче");
        String scrapperUrl = "http://localhost:8082/innerprosses/task/" + id + "/add-comment";

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

            request.setTaskId(id);
            request.setDate(LocalDateTime.now());

            ResponseEntity<CommentDTO> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(request, headers),
                    CommentDTO.class
            );

            System.out.println("Запрос на добавление нового комментария к задаче отправлен");
            return ResponseEntity.status(scrapperResponse.getStatusCode())
                    .body(scrapperResponse.getStatusCode().is2xxSuccessful()
                            ? scrapperResponse.getBody()
                            : "Ошибка при добавлении нового комментария к задаче");

        } catch (Exception e) {
            System.out.println("Запрос на добавление нового комментария к задаче не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка добавления нового комментария к задаче: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsByTask(@PathVariable Long id,
                                               @RequestHeader("Authorization") String authHeader) {
        System.out.println("Запрос прилетает: получение комментариев к задаче №" + id);

        String scrapperUrl = "http://localhost:8082/innerprosses/task/" + id + "/comments";

        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            if (!jwtUtil.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Недействительный токен");
            }

            ResponseEntity<List<CommentDTO>> scrapperResponse = restTemplate.exchange(
                    scrapperUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            System.out.println("Запрос на получение получение комментариев к задаче №" + id + " отправлен");
            return ResponseEntity.status(scrapperResponse.getStatusCode()).body(scrapperResponse.getBody());

        } catch (Exception e) {
            System.out.println("Запрос на получение комментариев к задаче №" + id + " не отправился");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка получения комментариев к задаче №" + id + e.getMessage());
        }
    }

}

//TODO: исправить баг невозможности редактировать текстовые поля задачи в режиме редактирование
//TODO: исправить обновление отображения задачи на вебе при её обновлении
