package edu.service;

import dto.GeneraleResponseDTO;
import dto.TaskDTO;
import edu.entity.Task;
import edu.entity.User;
import edu.repository.ProjectsRepository;
import edu.repository.TasksRepository;
import edu.repository.UsersRepository;
import edu.util.ConverterTaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private UsersRepository usersRepository;

    private final ConverterTaskDTO converter = new ConverterTaskDTO();

    public TaskDTO saveNew(TaskDTO taskDTO, String email) {

        Optional<User> optionalUser = usersRepository.findByEmail(email);
        User user = optionalUser.get();

        Task task = converter.convertTaskDTO(taskDTO, user);

        task = tasksRepository.save(task);

        return converter.convertTask(task);
    }

    public List<TaskDTO> getTasksByEmail(String email) {
        List<Task> recentTasks = tasksRepository.findAllByUserEmailOrderByDateLastView(email);
        return converter.convertTasks(recentTasks);
    }

    public void updateLastView(Long id) {
        tasksRepository.updateLastViewById(id);
    }

    public GeneraleResponseDTO updateTask(TaskDTO taskDTO) {
        try {
            Optional<Task> optionalTask = tasksRepository.findById(taskDTO.getTaskId());

            if (optionalTask.isEmpty()) {
                return new GeneraleResponseDTO(
                        "Пользователь с таким email не найден",
                        HttpStatus.NOT_FOUND.value(),
                        null
                );
            }

            Task task = optionalTask.get();
            task = converter.updateTaskFromTaskDTO(task, taskDTO);

            tasksRepository.save(task);
            return new GeneraleResponseDTO(
                    "Задача успешно обновлена",
                    HttpStatus.OK.value(),
                    null
            );

        } catch (Exception e) {
            return new GeneraleResponseDTO(
                    "Ошибка при обновлении задачи: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
        }
    }
}
