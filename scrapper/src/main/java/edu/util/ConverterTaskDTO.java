package edu.util;

import dto.task.TaskDTO;
import edu.entity.Project;
import edu.entity.Task;
import edu.entity.User;
import enums.TaskStatus;

import java.util.List;
import java.util.stream.Collectors;

public class ConverterTaskDTO {

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
        taskDTO.setStatus(task.getStatus());
        taskDTO.setDateCreate(task.getDateCreate());
        taskDTO.setDateLastView(task.getDateLastView());

        if (task.getAssignedTo() != null) {
            taskDTO.setAssignedTo(task.getAssignedTo().getUserId());
        }

        if (task.getProject() != null) {
            taskDTO.setProjectId(task.getProject().getProjectId());
        }

        return taskDTO;
    }

    public List<TaskDTO> convertTasks(List<Task> tasks) {
        return tasks.stream()
                .map(task -> {
                    assert task.getAssignedTo() != null;
                    return new TaskDTO(
                            task.getTaskId(),
                            task.getTaskName(),
                            task.getDescription(),
                            task.getStatus(),
                            task.getPriority(),
                            task.getProject().getProjectId(),
                            task.getProject().getProjectName(),
                            task.getAssignedTo().getUserId(),
                            task.getAssignedTo().getEmail(),
                            task.getStartDate(),
                            task.getEndDate(),
                            task.getCategory(),
                            task.getDateCreate(),
                            task.getDateLastView()
    //                        task.getDependencies()
                    );
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
        if (taskDTO.getAssignedTo() != null) {
            User user = new User();
            user.setUserId(taskDTO.getAssignedTo());
            task.setAssignedTo(user);
        }
        return task;
    }
}

