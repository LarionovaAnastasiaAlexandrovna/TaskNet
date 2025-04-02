package edu.service;

import dto.RegisterRequestDTO;
import edu.entity.User;
import edu.repository.UsersRepository;
import edu.util.ConverterRequestDTO;
import exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        } else {
            System.out.println("Пытаемся сделать repository.save(user)");
            //System.out.println("profilePhoto: " + user.getProfilePhoto());
            //System.out.println("profilePhoto length: " + (user.getProfilePhoto() != null ? user.getProfilePhoto().length : "null"));
            if (user.getProfilePhoto() != null) {
                user.setProfilePhoto(user.getProfilePhoto());
            }
            return repository.save(user);
        }
    }
}
