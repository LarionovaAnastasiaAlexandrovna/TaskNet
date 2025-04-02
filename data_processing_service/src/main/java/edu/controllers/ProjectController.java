package edu.controllers;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/project")
public class ProjectController {


    private final RestTemplate restTemplate;

    public ProjectController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

//    @PostMapping("/create")
//    public ResponseEntity<String> createProject(@RequestBody ProjectDTO project) {
//        String dbGatewayUrl = "http://localhost:8082/project/save";
//        ResponseEntity<String> response = restTemplate.postForEntity(dbGatewayUrl, project, String.class);
//
//        return ResponseEntity.ok("Project processed: " + response.getBody());
//    }
}
