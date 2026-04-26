package edu.controllers;

import dto.project.ProjectDTO;
import dto.user.UserDTO;
import edu.service.ScrapperProjectClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ScrapperProjectClient scrapperProjectClient;

    @PostMapping("/create")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> createProject(Authentication authentication,
                                           @RequestBody ProjectDTO request) {
        log.info("Запрос на создание проекта");

        try {
            String email = authentication.getName();
            log.debug("Email из токена: {}", email);

            var response = scrapperProjectClient.createProject(email, request);
            log.info("Проект успешно создан пользователем: {}", email);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Ошибка при создании проекта", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка создания проекта: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> getAllProject(Authentication authentication) {
        log.info("Запрос на получение всех проектов");

        try {
            String email = authentication.getName();
            log.debug("Email из токена: {}", email);

            var response = scrapperProjectClient.getAllProjects(email);
            log.info("Все проекты успешно получены для пользователя: {}", email);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Ошибка при получении всех проектов", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка получения всех связанных проектов: " + e.getMessage());
        }
    }

    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<?> getProjectTasks(@PathVariable Long projectId) {
        log.info("Запрос на получение задач проекта ID: {}", projectId);
        try {
            var tasks = scrapperProjectClient.getTasksByProjectId(projectId);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка получения задач проекта: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/all-users")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> getAllProjectUsers(@PathVariable Long id) {
        log.info("Запрос на получение данных о связанных пользователях проекта ID: {}", id);

        try {
            var response = scrapperProjectClient.getAllProjectUsers(id);
            log.info("Данные о пользователях проекта ID: {} успешно получены", id);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Ошибка при получении пользователей проекта ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка получения данных о связанных пользователях: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/add-user")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> addUserInProject(@PathVariable Long id,
                                              @RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        log.info("Добавление пользователя с email: {} в проект ID: {}", email, id);

        try {
            scrapperProjectClient.addUserToProject(id, email);
            log.info("Пользователь с email: {} успешно добавлен в проект ID: {}", email, id);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Ошибка при добавлении пользователя с email: {} в проект ID: {}", email, id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка добавления пользователя с email: " + email + " в проект ID: " + id + " " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{projectId}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> deleteUserProfile(@RequestBody UserDTO userDTO) {
        log.info("Запрос на обновление профиля пользователя: {}", userDTO.getEmail());

       return null;
    }
}
