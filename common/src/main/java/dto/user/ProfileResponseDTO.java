package dto.user;

import dto.GeneralResponseDTO;
import dto.task.TaskDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponseDTO extends GeneralResponseDTO {
    UserDTO userDTO;
    List<TaskDTO> tasks;
}
