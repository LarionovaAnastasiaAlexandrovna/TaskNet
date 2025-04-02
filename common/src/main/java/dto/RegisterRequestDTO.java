package dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO extends GeneralRequestDTO {
    @NotNull(message = "имя не может быть пустым")
    private String username;

    @NotNull(message = "email не может быть пустым")
    @Email(message = "некорректный формат email")
    private String email;

    @NotNull(message = "пароль не может быть пустым")
    private String password;
}
