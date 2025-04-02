package edu.controllers;

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


//    private final WebClient webClient;

//    public AuthUserController(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("http://localhost:8081").build();
//    }
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
//        userServise.registerUser(request);
//        return ResponseEntity.ok("User registered successfully");
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequestDTO request) {
//        boolean success = userServise.loginUser(request);
//        return success ? ResponseEntity.ok("Login successful") : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//    }
}