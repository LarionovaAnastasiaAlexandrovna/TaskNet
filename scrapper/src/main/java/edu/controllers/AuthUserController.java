package edu.controllers;

import dto.GeneralResponseDTO;
import dto.user.LoginRequestDTO;
import dto.user.RegisterRequestDTO;
import dto.user.RegisterResponseDTO;
import edu.entity.User;
import edu.service.AuthUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("interprocess/auth")
public class AuthUserController {
    private final AuthUserService usersService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        log.info("Запрос на регистрацию пришел в scrapper");
            User savedUser = usersService.save(registerRequestDTO);

        RegisterResponseDTO response = RegisterResponseDTO.builder()
                .userId(savedUser.getUserId())
                .name(savedUser.getUserName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("Запрос на вход пришел в scrapper");
        User user = usersService.login(loginRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestParam String email) {
        log.info("Запрос на верификацию пришел в scrapper");
        boolean isVerify = usersService.verify(email);
        return ResponseEntity.status(HttpStatus.OK).body(new GeneralResponseDTO(isVerify ? "Почта успешно подтверждена!" : "Что-то пошло не так", 200, null));
    }

}