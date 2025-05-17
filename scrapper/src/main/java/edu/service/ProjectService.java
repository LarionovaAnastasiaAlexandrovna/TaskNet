package edu.service;

import dto.ProjectDTO;
import dto.UserInProjectDTO;
import edu.entity.Project;
import edu.entity.ProjectUser;
import edu.entity.User;
import edu.repository.ProjectUserRepository;
import edu.repository.ProjectsRepository;
import edu.repository.UsersRepository;
import edu.util.ConverterUserRequestDTO;
import edu.util.ConverterRequestProjectDTO;
import enums.ProjectStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProjectService {
    @Autowired
    private ProjectsRepository projectsRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    private final ConverterRequestProjectDTO converter = new ConverterRequestProjectDTO();
    private final ConverterUserRequestDTO converterUser = new ConverterUserRequestDTO();

    public ProjectDTO saveNew(ProjectDTO createProjectRequestDTO, String email) {

        Project project = converter.convertProjectDTO(createProjectRequestDTO);

        project.setStatus(ProjectStatus.CREATE);

        project = projectsRepository.save(project);

        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        ProjectUser projectUser = new ProjectUser();
        projectUser.setUser(user);
        projectUser.setProject(project);
        projectUserRepository.save(projectUser);

        return converter.convertProject(project);
    }

    public List<ProjectDTO> getProjectsByEmail(String email) {
        List<Project> projects = projectsRepository.findAllByUserEmail(email);
        return projects.stream()
                .map(converter::convertProject)
                .collect(Collectors.toList());
    }

    public List<UserInProjectDTO> getUsersByProjectId(Long id) {
        List<User> users = projectsRepository.findAllByProjectId(id);
        return users.stream()
                .map(converterUser::convertUserInProject)
                .collect(Collectors.toList());
    }

    public void addUserInProjectByEmail(String email, Long id) {
        projectsRepository.addUserByEmail(email, id);
    }
}
