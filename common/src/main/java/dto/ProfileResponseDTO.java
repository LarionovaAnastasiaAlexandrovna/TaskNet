package dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfileResponseDTO extends GeneraleResponseDTO {
    UserDTO userDTO;
    List<TaskDTO> tasks;
}
