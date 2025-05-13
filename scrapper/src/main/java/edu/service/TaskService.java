package edu.service;

import dto.TaskDTO;
import edu.entity.Task;
import edu.repository.TasksRepository;
import edu.util.ConverterTaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TasksRepository tasksRepository;

    private final ConverterTaskDTO converter = new ConverterTaskDTO();

    public TaskDTO saveNew(TaskDTO taskDTO) {

        Task task = converter.convertTaskDTO(taskDTO);

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
}
