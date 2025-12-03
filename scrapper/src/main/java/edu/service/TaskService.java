package edu.service;

import dto.comment.CommentDTO;
import dto.GeneraleResponseDTO;
import dto.task.TaskDTO;
import edu.entity.Comment;
import edu.entity.Task;
import edu.entity.User;
import edu.repository.CommentsRepository;
import edu.repository.TasksRepository;
import edu.repository.UsersRepository;
import edu.util.ConverterCommentDTO;
import edu.util.ConverterTaskDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TaskService {
    private final TasksRepository tasksRepository;

    private final UsersRepository usersRepository;

    private final CommentsRepository commentsRepository;

    private final ConverterTaskDTO converter = new ConverterTaskDTO();

    private final ConverterCommentDTO converterCommentDTO = new ConverterCommentDTO();

    public TaskDTO saveNew(TaskDTO taskDTO, String email) {

        Optional<User> optionalUser = usersRepository.findByEmail(email);
        User user = optionalUser.get();

        Task task = converter.convertTaskDTO(taskDTO, user);

        task = tasksRepository.save(task);

        return converter.convertTask(task);
    }

    public CommentDTO saveNew(CommentDTO commentDTO, String email) {

        Optional<User> optionalUser = usersRepository.findByEmail(email);
        User user = optionalUser.get();

        Optional<Task> optionalTask = tasksRepository.findById(commentDTO.getTaskId());
        Task task = optionalTask.get();

        Comment comment = converterCommentDTO.convertCommentDTO(commentDTO, user, task);

        comment = commentsRepository.save(comment);

        return converterCommentDTO.convertComment(comment);
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

    public List<CommentDTO> getAllCommentsByTackId(Long id) {
        return converterCommentDTO.convertComments(commentsRepository.getCommentsByTaskTaskId(id));
    }
}
