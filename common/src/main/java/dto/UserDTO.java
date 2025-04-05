package dto;

import enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userName;

    private String email;

    private UserRole role;

    private String socialLinks;

    private LocalDate birthDate;

    private byte[] profilePhoto;

    private String phoneNumber;

    private String location;

    private String profileDescription;
}
