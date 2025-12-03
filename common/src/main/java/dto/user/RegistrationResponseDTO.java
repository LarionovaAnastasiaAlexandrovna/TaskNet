package dto.user;

import dto.GeneraleResponseDTO;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationResponseDTO extends GeneraleResponseDTO {
    String message;
}

