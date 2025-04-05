package edu.service;

import dto.ProfileResponseDTO;
import dto.TaskDTO;
import dto.UserDTO;
import edu.entity.Task;
import edu.entity.User;
import edu.repository.TasksRepository;
import edu.repository.UsersRepository;
import edu.util.ConverterRequestProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileUserServise {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TasksRepository tasksRepository;

    private final ConverterRequestProfileDTO converter = new ConverterRequestProfileDTO();

    public ProfileResponseDTO getProfileByEmail(String email) {
        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        UserDTO userDTO = converter.convertUser(user);

        List<Task> tasks = tasksRepository.findAllByAssignedTo(user);

        List<TaskDTO> taskDTOs = converter.convertTasks(tasks);

        return new ProfileResponseDTO(userDTO, taskDTOs);
    }
}