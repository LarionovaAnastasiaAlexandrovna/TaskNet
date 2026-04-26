package edu.controllers;

import dto.task.TaskDTO;
import dto.user.EmailRequestDTO;
import dto.project.ProjectDTO;
import dto.project.UserInProjectDTO;
import edu.service.ProjectService;
import edu.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("interprocess/project")
public class ProjectUserController {
    private final ProjectService projectService;
    private final TaskService taskService;

    public ProjectUserController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDTO createProjectRequestDTO,
                                           @RequestHeader("X-User-Email") String email) {
        log.info("Запрос на регистрацию пришел в scrapper");
        ProjectDTO saveProject = projectService.saveNew(createProjectRequestDTO, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveProject);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProject(@RequestHeader("X-User-Email") String email) {
        log.info("Scrapper получил email: {}", email);
        try {
            List<ProjectDTO> projectDTOS = projectService.getProjectsByEmail(email);
            return ResponseEntity.ok(projectDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка Scrapper-сервиса: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/tasks")
    public ResponseEntity<?> getTasksByProjectId(@PathVariable Long id) {
        log.info("Запрос на получение задач проекта №{} в scrapper", id);
        try {
            List<TaskDTO> tasks = taskService.getTasksByProjectId(id);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка Scrapper-сервиса: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/all-users")
    public ResponseEntity<?> getAllProjectUsers(@PathVariable Long id) {
        log.info(MessageFormat.format("Scrapper получил id: {0}", id));
        try {
            List<UserInProjectDTO> users = projectService.getUsersByProjectId(id);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка Scrapper-сервиса: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/add-user")
    public ResponseEntity<?> addUserInProject(@PathVariable Long id,
                                              @RequestBody EmailRequestDTO request) {
        log.info("Scrapper получил email: {}", request.getEmail());
        try {
            projectService.addUserInProjectByEmail(request.getEmail(), id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка Scrapper-сервиса: " + e.getMessage());
        }
    }
}
