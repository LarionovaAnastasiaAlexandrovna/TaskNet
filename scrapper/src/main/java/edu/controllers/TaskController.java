package edu.controllers;

import dto.CommentDTO;
import dto.GeneraleResponseDTO;
import dto.TaskDTO;
import edu.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("innerprosses/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@RequestHeader("X-User-Email") String email,
                                           @Valid @RequestBody TaskDTO taskDTO) {
        System.out.println("Запрос на создание новой задачи пришел в scrapper");
        TaskDTO saveTaskDTO = taskService.saveNew(taskDTO, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveTaskDTO);
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentTasks(@RequestHeader("X-User-Email") String email) {
        System.out.println("Scrapper получил email: " + email);
        System.out.println("Запрос на получение недавних задач пришел в scrapper");
        try {
            List<TaskDTO> taskDTOs = taskService.getTasksByEmail(email);
            return ResponseEntity.ok(taskDTOs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка Scrapper-сервиса: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/view")
    public ResponseEntity<?> updateLastView(@PathVariable Long id) {
        System.out.println("Запрос на обновления последнего просмотра задачи пришел в scrapper");
        try {
            taskService.updateLastView(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка Scrapper-сервиса: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateTask(@RequestBody TaskDTO taskDTO) {
        try {
            GeneraleResponseDTO responseDTO = taskService.updateTask(taskDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка Scrapper-сервиса: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/add-comment")
    public ResponseEntity<?> addComment(@RequestHeader("X-User-Email") String email,
                                        @Valid @RequestBody CommentDTO commentDTO) {
        System.out.println("Запрос на создание нового комментария пришел в scrapper");
        CommentDTO saveCommentDTO = taskService.saveNew(commentDTO, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveCommentDTO);
    }

    //        String scrapperUrl = "http://localhost:8082/innerprosses/task/" + id + "/comments";
    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getCommentsByTask(@PathVariable Long id) {
        System.out.println("Запрос на получение комментариев по задаче №" + id + " пришел в scrapper");
        try {
            List<CommentDTO> commentDTOS = taskService.getAllCommentsByTackId(id);
            return ResponseEntity.ok(commentDTOS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка Scrapper-сервиса: " + e.getMessage());
        }
    }
}
