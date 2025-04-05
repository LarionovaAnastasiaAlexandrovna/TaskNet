package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDTO extends GeneraleResponseDTO {
    private UserDTO userDTO;

    private List<TaskDTO> tasks;
}
