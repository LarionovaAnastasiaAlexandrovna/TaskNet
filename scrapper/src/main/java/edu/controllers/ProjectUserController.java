package edu.controllers;

import dto.ProjectDTO;
import edu.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("innerprosses/project")
public class ProjectUserController {
    private final ProjectService projectService;

    public ProjectUserController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectDTO createProjectRequestDTO) {
        System.out.println("Запрос на регистрацию пришел в scrapper");
        ProjectDTO saveProject = projectService.saveNew(createProjectRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveProject);
    }
}
