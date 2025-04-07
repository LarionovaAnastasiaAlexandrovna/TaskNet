package edu.controllers;

import dto.GeneraleResponseDTO;
import dto.ProfileResponseDTO;
import dto.UserDTO;
import edu.service.ProfileUserServise;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("innerprosses/profile")
public class ProfileUserController {

    private final ProfileUserServise profileServise;

    public ProfileUserController(ProfileUserServise profileServise) {
        this.profileServise = profileServise;
    }

    @GetMapping
    public ResponseEntity<?> getUserProfile(@RequestHeader("X-User-Email") String email) {
        System.out.println("Scrapper получил email: " + email);
        try {
            ProfileResponseDTO responseDTO = profileServise.getProfileByEmail(email);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка Scrapper-сервиса: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserProfile(@RequestBody UserDTO userDTO) {
        try {
            GeneraleResponseDTO responseDTO = profileServise.updateProfile(userDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка Scrapper-сервиса: " + e.getMessage());
        }
    }
}
