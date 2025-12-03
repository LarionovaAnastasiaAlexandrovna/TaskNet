package dto.user;

import annotation.Email;
import dto.GeneralRequestDTO;
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
public class LoginRequestDTO extends GeneralRequestDTO {
    @NotNull(message = "email не может быть пустым")
    @Email
    String email;

    @NotNull(message = "пароль не может быть пустым")
    String password;
}