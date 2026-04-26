package edu.controllers;

import dto.comment.CommentDTO;
import dto.task.TaskDTO;
import dto.user.UserDTO;
import edu.service.ScrapperTaskClient;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final ScrapperTaskClient scrapperTaskClient;

    @PostMapping("/create")
    public ResponseEntity<?> createTask(Authentication authentication,
                                        @RequestBody TaskDTO request) {
        log.info("Запрос на создание задачи");

        try {
            String email = authentication.getName();
            log.debug("Email из токена: {}", email);

            var response = scrapperTaskClient.createTask(email, request);
            log.info("Задача успешно создана пользователем: {}", email);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Ошибка при создании задачи", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка создания задачи: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        log.info("Запрос на получение задачи ID: {}", id);
        try {
            var response = scrapperTaskClient.getTaskById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Ошибка при получении задачи ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка получения задачи: " + e.getMessage());
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentTasks(Authentication authentication) {
        log.info("Запрос на получение недавних задач");

        try {
            String email = authentication.getName();
            log.debug("Email из токена: {}", email);

            var response = scrapperTaskClient.getRecentTasks(email);
            log.info("Недавние задачи успешно получены для пользователя: {}", email);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Ошибка при получении недавних задач", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка получения недавних задач: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/view")
    public ResponseEntity<?> updateLastView(@PathVariable Long id) {
        log.info("Запрос на обновление последнего просмотра задачи ID: {}", id);

        try {
            scrapperTaskClient.updateLastView(id);
            log.info("Время просмотра задачи ID: {} успешно обновлено", id);

            return ResponseEntity.ok().build();

        } catch (Exception e) {
            log.error("Ошибка при обновлении просмотра задачи ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка создания задачи: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateTask(@PathVariable Long id,
                                        @RequestBody TaskDTO taskDTO) {
        log.info("Запрос на обновление задачи ID: {}", id);

        try {
            var response = scrapperTaskClient.updateTask(id, taskDTO);
            log.info("Задача ID: {} успешно обновлена", id);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Ошибка при обновлении задачи ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка обновления задачи: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/add-comment")
    public ResponseEntity<?> addComment(@PathVariable Long id,
                                        Authentication authentication,
                                        @RequestBody CommentDTO request) {
        log.info("Запрос на добавление комментария к задаче ID: {}", id);

        try {
            String email = authentication.getName();
            log.debug("Email из токена: {}", email);

            // Устанавливаем ID задачи и дату
            request.setTaskId(id);
            request.setDate(LocalDateTime.now());

            var response = scrapperTaskClient.addComment(id, email, request);
            log.info("Комментарий успешно добавлен к задаче ID: {}", id);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Ошибка при добавлении комментария к задаче ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка добавления нового комментария к задаче: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsByTask(@PathVariable Long id) {
        log.info("Запрос на получение комментариев к задаче ID: {}", id);

        try {
            var response = scrapperTaskClient.getCommentsByTask(id);
            log.info("Комментарии к задаче ID: {} успешно получены", id);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Ошибка при получении комментариев задачи ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка получения комментариев к задаче №" + id + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{taskId}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> deleteUserProfile(@RequestBody UserDTO userDTO) {
        log.info("Запрос на обновление профиля пользователя: {}", userDTO.getEmail());

        return null;
    }

}

//TODO: исправить баг невозможности редактировать текстовые поля задачи в режиме редактирование
//TODO: исправить обновление отображения задачи на вебе при её обновлении
