package edu.controllers;

import dto.LoginRequestDTO;
import dto.RegisterRequestDTO;
import edu.entity.User;
import edu.service.AuthUserServise;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("innerprosses/auth")
//@RequiredArgsConstructor
public class AuthUserController {
    private final AuthUserServise usersService;

    public AuthUserController(AuthUserServise usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        System.out.println("Запрос на регистрацию пришел в scrapper");
            User savedUser = usersService.save(registerRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        System.out.println("Запрос на вход пришел в scrapper");
        User user = usersService.login(loginRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}