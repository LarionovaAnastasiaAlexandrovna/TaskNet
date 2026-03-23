package edu.service;

import dto.user.LoginRequestDTO;
import dto.user.RegisterRequestDTO;
import edu.entity.User;
import edu.repository.UsersRepository;
import edu.util.ConverterUserRequestDTO;
import exception.UserException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthUserService {

    private final UsersRepository repository;

    private final ConverterUserRequestDTO converter = new ConverterUserRequestDTO();

    public AuthUserService(UsersRepository repository) {
        this.repository = repository;
    }

    public User save(RegisterRequestDTO registerRequestDTO) {
        System.out.println("Пытаемся сделать user = converter.convert(registerRequestDTO)");
        User user = converter.convert(registerRequestDTO);
        if (repository.findByEmail(user.getEmail()).isPresent()
        ) {
            throw new UserException(UserException.Type.USER_ALREADY_EXISTS, "Пользователь с почтой " + user.getEmail() + " уже существует");
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
        if (!user.isEnabled()) {
            throw new UserException(UserException.Type.USER_NOT_FOUND, "Пользователь не подтвердил почту");
        }
        System.out.println("Пытаемся проверить пароль");
        if(!Objects.equals(loginRequestDTO.getPassword(), user.getPasswordHash())) {
            throw new UserException(UserException.Type.INVALID_PASSWORD, "Неверный пароль");
        }
        System.out.println("Авторизация успешна!");
        return user;
    }

    public boolean verify(String email) {
        System.out.println("Пытаемся найти пользователя по email");
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserException.Type.USER_NOT_FOUND, "Пользователь не найден"));
        user.setEnabled(true);
        repository.save(user);
        return true;
    }
}
