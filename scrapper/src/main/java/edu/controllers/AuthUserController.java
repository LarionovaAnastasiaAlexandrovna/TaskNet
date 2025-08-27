package edu.controllers;

import dto.GeneraleResponseDTO;
import dto.LoginRequestDTO;
import dto.RegisterRequestDTO;
import edu.entity.User;
import edu.service.AuthUserServise;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("innerprosses/auth")
public class AuthUserController {
    private final AuthUserServise usersService;

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

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String email) {
        System.out.println("Запрос на верификацию пришел в scrapper");
        boolean isVerify = usersService.verify(email);
        return ResponseEntity.status(HttpStatus.OK).body(new GeneraleResponseDTO(isVerify ? "Почта успешно подтверждена!" : "Что-то пошло не так", 200, null));
    }

}