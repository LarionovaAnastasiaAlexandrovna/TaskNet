package edu.controllers;

import dto.ProjectDTO;
import dto.TaskDTO;
import edu.service.ProjectService;
import edu.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("innerprosses/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProject(@Valid @RequestBody TaskDTO taskDTO) {
        System.out.println("Запрос на создание новой задачи пришел в scrapper");
        TaskDTO saveTaskDTO = taskService.saveNew(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveTaskDTO);
    }
    }
