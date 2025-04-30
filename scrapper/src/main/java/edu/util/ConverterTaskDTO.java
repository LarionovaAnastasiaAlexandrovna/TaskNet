package edu.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.TaskDTO;
import edu.entity.Project;
import edu.entity.Task;
import edu.entity.User;
import enums.TaskStatus;

public class ConverterTaskDTO {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Task convertTaskDTO(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTaskName(taskDTO.getTaskName());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        task.setStartDate(taskDTO.getStartDate());
        task.setEndDate(taskDTO.getEndDate());
        task.setCategory(taskDTO.getCategory());
        task.setStatus(TaskStatus.OPEN);

        if (taskDTO.getUserId() != null) {
            User user = new User();
            user.setUserId(taskDTO.getUserId());
            task.setAssignedTo(user);
        }

        if (taskDTO.getProjectId() != null) {
            Project project = new Project();
            project.setProjectId(taskDTO.getProjectId());
            task.setProject(project);
        }

        return task;
    }

    public TaskDTO convertTask(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName(task.getTaskName());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setStartDate(task.getStartDate());
        taskDTO.setEndDate(task.getEndDate());
        taskDTO.setCategory(task.getCategory());
        taskDTO.setStatus(task.getStatus());

        if (task.getAssignedTo() != null) {
            taskDTO.setUserId(task.getAssignedTo().getUserId());
        }

        if (task.getProject() != null) {
            taskDTO.setProjectId(task.getProject().getProjectId());
        }

        return taskDTO;
    }
}

