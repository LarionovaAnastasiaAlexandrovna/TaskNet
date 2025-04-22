package edu.service;

import dto.ProjectDTO;
import edu.entity.Project;
import edu.repository.ProjectsRepository;
import edu.util.ConverterRequestProjectDTO;
import enums.ProjectStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ProjectService {
    @Autowired
    private ProjectsRepository projectsRepository;

    private final ConverterRequestProjectDTO converter = new ConverterRequestProjectDTO();

    public ProjectDTO saveNew(ProjectDTO createProjectRequestDTO) {

        Project project = converter.convertProjectDTO(createProjectRequestDTO);

        project.setStatus(ProjectStatus.CREATE);

        project = projectsRepository.save(project);

        return converter.convertProject(project);
    }
}
