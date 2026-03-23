package edu.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.user.RegisterRequestDTO;
import edu.entity.User;
import edu.service.AuthUserService;
import enums.UserRole;
import exception.UserException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthUserController.class)
class AuthUserControllerTest {

    @MockitoBean
    private AuthUserService authUserService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnCreatedUser_whenValidRegistrationRequest() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                "username",
                "email@test.ru",
                "password");
        User savedUser = User.builder()
                .userId(1L)
                .userName("username")
                .email("email@test.ru")
                .role(UserRole.USER)
                .passwordHash("hashedPassword")
                .build();

        when(authUserService.save(registerRequestDTO)).thenReturn(savedUser);

        mockMvc.perform(post("/interprocess/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.name").value("username"))
                .andExpect(jsonPath("$.email").value("email@test.ru"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.passwordHash").doesNotExist());
    }

    @Test
    void shouldReturnConflict_whenUserAlreadyExists() throws Exception {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                "username",
                "email@test.ru",
                "password");

        doThrow(new UserException(UserException.Type.USER_ALREADY_EXISTS, "Пользователь с почтой email@test.ru уже существует"))
                .when(authUserService).save(registerRequestDTO);

        mockMvc.perform(post("/interprocess/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequestDTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value("Пользователь с почтой email@test.ru уже существует"));
    }

    @Test
    void loginUser() {
    }

    @Test
    void verifyEmail() {
    }
}