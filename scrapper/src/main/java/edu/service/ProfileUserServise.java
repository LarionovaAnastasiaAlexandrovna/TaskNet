package edu.service;

import dto.GeneraleResponseDTO;
import dto.ProfileResponseDTO;
import dto.TaskDTO;
import dto.UserDTO;
import edu.entity.Task;
import edu.entity.User;
import edu.repository.TasksRepository;
import edu.repository.UsersRepository;
import edu.util.ConverterRequestUserDTO;
import edu.util.ConverterTaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileUserServise {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TasksRepository tasksRepository;

    private final ConverterTaskDTO converterTask = new ConverterTaskDTO();

    private final ConverterRequestUserDTO converterUser = new ConverterRequestUserDTO();

    public ProfileResponseDTO getProfileByEmail(String email) {
        User user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        UserDTO userDTO = converterUser.convertUser(user);

        List<Task> tasks = tasksRepository.findAllByAssignedTo(user);

        List<TaskDTO> taskDTOs = converterTask.convertTasks(tasks);

        return new ProfileResponseDTO(userDTO, taskDTOs);
    }

    public GeneraleResponseDTO updateProfile(UserDTO userDTO) {
        try {
            Optional<User> optionalUser = usersRepository.findByEmail(userDTO.getEmail());

            if (optionalUser.isEmpty()) {
                return new GeneraleResponseDTO(
                        "Пользователь с таким email не найден",
                        HttpStatus.NOT_FOUND.value(),
                        null
                );
            }

            User user = optionalUser.get();

            user.setUserName(userDTO.getUserName());
            user.setSocialLinks(userDTO.getSocialLinks());
            user.setPhoneNumber(userDTO.getPhoneNumber());
            user.setLocation(userDTO.getLocation());
            user.setBirthDate(userDTO.getBirthDate());
            user.setProfileDescription(userDTO.getProfileDescription());
            // user.setProfilePhoto(userDTO.getProfilePhoto()); // если редактируем фото

            usersRepository.save(user);
                return new GeneraleResponseDTO(
                        "Профиль успешно обновлён",
                        HttpStatus.OK.value(),
                        null
                );

        } catch (Exception e) {
            return new GeneraleResponseDTO(
                    "Ошибка при обновлении профиля: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            );
        }
    }
}