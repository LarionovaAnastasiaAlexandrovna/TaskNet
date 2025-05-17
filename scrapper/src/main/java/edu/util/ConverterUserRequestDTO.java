package edu.util;

import dto.LoginRequestDTO;
import dto.RegisterRequestDTO;
import dto.UserInProjectDTO;
import edu.entity.User;
import enums.UserRole;

public class ConverterUserRequestDTO {
    public User convert(RegisterRequestDTO registerRequestDTO) {
        User user = new User();
        user.setUserName(registerRequestDTO.getUsername());
        user.setEmail(registerRequestDTO.getEmail());
        String passwordHash = registerRequestDTO.getPassword(); // TODO написать шифровальщик, а потом хеширование
        user.setPasswordHash(passwordHash);
        user.setProfilePhoto(null);
        user.setRole(UserRole.USER);
        return user;
    }

    public User convert(LoginRequestDTO loginRequestDTO) {
        User user = new User();
        user.setEmail(loginRequestDTO.getEmail());
        String passwordHash = loginRequestDTO.getPassword(); // TODO написать шифровальщик, а потом хеширование
        user.setPasswordHash(passwordHash);
        return user;
    }

    public UserInProjectDTO convertUserInProject(User user) {
        UserInProjectDTO userDTO = new UserInProjectDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserName(user.getUserName());
        return userDTO;
    }
}
