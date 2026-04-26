package edu.util;

import dto.task.TaskDTO;
import edu.entity.Project;
import edu.entity.Task;
import edu.entity.User;
import edu.repository.UsersRepository;
import enums.TaskStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@AllArgsConstructor
@Component
public class ConverterTaskDTO {

    private final UsersRepository usersRepository;


    public Task convertTaskDTO(TaskDTO taskDTO, User user) {
        Task task = new Task();
        task.setTaskId(taskDTO.getTaskId());
        task.setTaskName(taskDTO.getTaskName());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setPriority(taskDTO.getPriority());
        task.setStartDate(taskDTO.getStartDate());
        task.setEndDate(taskDTO.getEndDate());
        task.setCategory(taskDTO.getCategory());
        task.setStatus(TaskStatus.OPEN);
        task.setDateCreate(taskDTO.getDateCreate());
        task.setDateLastView(taskDTO.getDateLastView());
        task.setAssignedTo(user);
        task.setEstimatedHours(taskDTO.getEstimatedHours());
        task.setCreatedBy(user.getUserId());
        // Устанавливаем исполнителя, если передан
        if (taskDTO.getAssignedTo() != null) {
            Optional<User> assignee = usersRepository.findById(taskDTO.getAssignedTo());
            assignee.ifPresent(task::setAssignedTo);
        } else {
            task.setAssignedTo(null);
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
        taskDTO.setTaskId(task.getTaskId());
        taskDTO.setTaskName(task.getTaskName());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setPriority(task.getPriority());
        taskDTO.setStartDate(task.getStartDate());
        taskDTO.setEndDate(task.getEndDate());
        taskDTO.setCategory(task.getCategory());
        taskDTO.setDateCreate(task.getDateCreate());
        taskDTO.setDateLastView(task.getDateLastView());
        taskDTO.setEstimatedHours(task.getEstimatedHours());

        if (task.getAssignedTo() != null) {
            taskDTO.setAssignedTo(task.getAssignedTo().getUserId());
            taskDTO.setEmail(task.getAssignedTo().getEmail());
        }

        if (task.getProject() != null) {
            taskDTO.setProjectId(task.getProject().getProjectId());
            taskDTO.setProjectName(task.getProject().getProjectName());
        }

        return taskDTO;
    }

    public List<TaskDTO> convertTasks(List<Task> tasks) {
        return tasks.stream()
                .map(task -> {
                    TaskDTO dto = new TaskDTO();
                    dto.setTaskId(task.getTaskId());
                    dto.setTaskName(task.getTaskName());
                    dto.setDescription(task.getDescription());
                    dto.setStatus(task.getStatus());
                    dto.setPriority(task.getPriority());
                    dto.setStartDate(task.getStartDate());
                    dto.setEndDate(task.getEndDate());
                    dto.setCategory(task.getCategory());
                    dto.setDateCreate(task.getDateCreate());
                    dto.setDateLastView(task.getDateLastView());
                    dto.setEstimatedHours(task.getEstimatedHours());

                    if (task.getAssignedTo() != null) {
                        dto.setAssignedTo(task.getAssignedTo().getUserId());
                        dto.setEmail(task.getAssignedTo().getEmail());
                    }
                    if (task.getProject() != null) {
                        dto.setProjectId(task.getProject().getProjectId());
                        dto.setProjectName(task.getProject().getProjectName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Task updateTaskFromTaskDTO(Task task, TaskDTO taskDTO) {
        if (taskDTO.getTaskName() != null) {
            task.setTaskName(taskDTO.getTaskName());
        }
        if (taskDTO.getDescription() != null) {
            task.setDescription(taskDTO.getDescription());
        }
        if (taskDTO.getCategory() != null) {
            task.setCategory(taskDTO.getCategory());
        }
        if (taskDTO.getPriority() != null) {
            task.setPriority(taskDTO.getPriority());
        }
        if (taskDTO.getStatus() != null) {
            task.setStatus(taskDTO.getStatus());
        }
        if (taskDTO.getStartDate() != null) {
            task.setStartDate(taskDTO.getStartDate());
        }
        if (taskDTO.getEndDate() != null) {
            task.setEndDate(taskDTO.getEndDate());
        }
        if (taskDTO.getEstimatedHours() != null) {
            task.setEstimatedHours(taskDTO.getEstimatedHours());
        }
        if (taskDTO.getAssignedTo() != null) {
            User user = new User();
            user.setUserId(taskDTO.getAssignedTo());
            task.setAssignedTo(user);
        }
        if (taskDTO.getProjectId() != null) {
            Project project = new Project();
            project.setProjectId(taskDTO.getProjectId());
            task.setProject(project);
        }
        return task;
    }
}

