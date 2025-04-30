package edu.util;

import dto.TaskDTO;
import dto.UserDTO;
import edu.entity.Task;
import edu.entity.User;

import java.util.stream.Collectors;
import java.util.List;


public class ConverterRequestProfileDTO {

    public UserDTO convertUser(User user) {
        return new UserDTO(
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                user.getSocialLinks(),
                user.getBirthDate(),
                user.getProfilePhoto(),
                user.getPhoneNumber(),
                user.getLocation(),
                user.getProfileDescription()
        );
    }

    public List<TaskDTO> convertTasks(List<Task> tasks) {
        return tasks.stream()
                .map(task -> new TaskDTO(
                        task.getTaskName(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getProject().getProjectId(),
                        task.getAssignedTo() != null ? task.getAssignedTo().getUserId() : null,
                        task.getStartDate(),
                        task.getEndDate(),
                        task.getCategory()
//                        task.getDependencies()
                ))
                .collect(Collectors.toList());
    }
}
