package dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequestDTO extends GeneralRequestDTO {
    @NotNull(message = "имя не может быть пустым")
    String username;

    @NotNull(message = "email не может быть пустым")
    @Email(message = "некорректный формат email")
    String email;

    @NotNull(message = "пароль не может быть пустым")
    String password;
}
