package dto.user;

import enums.UserRole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
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
