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
public class LoginRequestDTO extends GeneralRequestDTO {
    @NotNull(message = "email не может быть пустым")
    @Email(message = "Некорректный формат email")
    String email;

    @NotNull(message = "пароль не может быть пустым")
    String password;
}