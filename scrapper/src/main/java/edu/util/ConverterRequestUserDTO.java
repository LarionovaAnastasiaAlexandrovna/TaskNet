package edu.util;

import dto.user.UserDTO;
import edu.entity.User;

public class ConverterRequestUserDTO {

    public UserDTO convertUser(User user) {
        return new UserDTO(
                user.getUserName(),
                user.getEmail(),
                user.getRole(),
                user.getSocialLinks(),
                user.getBirthDate(),
                user.getProfilePhoto(),
                user.getPhoneNumber(),
                user.getLocation(),
                user.getProfileDescription()
        );
    }
}
