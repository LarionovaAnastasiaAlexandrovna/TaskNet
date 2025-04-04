package edu.service;

import dto.LoginRequestDTO;
import dto.RegisterRequestDTO;
import edu.entity.User;
import edu.repository.UsersRepository;
import edu.util.ConverterRequestDTO;
import exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthUserServise {

    @Autowired
    private UsersRepository repository;

    private final ConverterRequestDTO converter = new ConverterRequestDTO();

    public User save(RegisterRequestDTO registerRequestDTO) {
        System.out.println("Пытаемся сделать user = converter.convert(registerRequestDTO)");
        User user = converter.convert(registerRequestDTO);
        if (repository.findByEmail(user.getEmail()).isPresent()
                //repository.isExistUserByEmail(user.getEmail())
        ) {
            throw new UserException(UserException.Type.USER_ALREADY_EXISTS, "Пользователь уже существует");
        } else {
            System.out.println("Пытаемся сделать repository.save(user)");
            if (user.getProfilePhoto() != null) {
                user.setProfilePhoto(user.getProfilePhoto());
            }
            return repository.save(user);
        }
    }

    public User login(LoginRequestDTO loginRequestDTO) {
        System.out.println("Пытаемся найти пользователя по email");
        User user = repository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(() -> new UserException(UserException.Type.USER_NOT_FOUND, "Пользователь не найден"));
        System.out.println("Пытаемся проверить пароль");
        if(!Objects.equals(loginRequestDTO.getPassword(), user.getPasswordHash())) {
            throw new UserException(UserException.Type.INVALID_PASSWORD, "Неверный пароль");
        }
        System.out.println("Авторизация успешна!");
        return user;
    }
}
