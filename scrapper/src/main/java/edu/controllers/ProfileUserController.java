package edu.controllers;

import dto.GeneralResponseDTO;
import dto.user.ProfileResponseDTO;
import dto.user.UserDTO;
import edu.service.ProfileUserServise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("interprocess/profile")
public class ProfileUserController {

    private final ProfileUserServise profileServise;

    public ProfileUserController(ProfileUserServise profileServise) {
        this.profileServise = profileServise;
    }

    @GetMapping
    public ResponseEntity<?> getUserProfile(@RequestHeader("X-User-Email") String email) {
        log.info("Scrapper получил email: " + email);
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
            GeneralResponseDTO responseDTO = profileServise.updateProfile(userDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка Scrapper-сервиса: " + e.getMessage());
        }
    }
}
