package edu.controllers;

import dto.ProjectDTO;
import edu.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("innerprosses/project")
public class ProjectUserController {
    private final ProjectService projectService;

    public ProjectUserController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDTO createProjectRequestDTO,
                                           @RequestHeader("X-User-Email") String email) {
        System.out.println("Запрос на регистрацию пришел в scrapper");
        ProjectDTO saveProject = projectService.saveNew(createProjectRequestDTO, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveProject);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllProject(@RequestHeader("X-User-Email") String email) {
        System.out.println("Scrapper получил email: " + email);
        try {
            List<ProjectDTO> projectDTOS = projectService.getProjectsByEmail(email);
            return ResponseEntity.ok(projectDTOS);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка Scrapper-сервиса: " + e.getMessage());
        }
    }
}
