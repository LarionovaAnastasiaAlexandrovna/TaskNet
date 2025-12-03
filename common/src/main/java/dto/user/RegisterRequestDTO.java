package dto.user;

import dto.GeneralRequestDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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
