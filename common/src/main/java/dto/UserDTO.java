package dto;

import enums.UserRole;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    String userName;

    String email;

    UserRole role;

    String socialLinks;

    LocalDate birthDate;

    byte[] profilePhoto;

    String phoneNumber;

    String location;

    String profileDescription;
}
