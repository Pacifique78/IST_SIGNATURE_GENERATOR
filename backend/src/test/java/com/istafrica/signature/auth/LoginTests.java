package com.istafrica.signature.auth;

import com.istafrica.signature.dto.auth.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginTests extends AuthTestBase {

    @Test
    void login_WithVerifiedUser_ShouldSucceed() throws Exception {
        // Arrange
        userRepository.save(testDataBuilder.createVerifiedUser());

        LoginRequest request = new LoginRequest();
        request.setEmail(VALID_EMAIL);
        request.setPassword(VALID_PASSWORD);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.email").value(VALID_EMAIL))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void login_WithUnverifiedUser_ShouldReturn401() throws Exception {
        // Arrange
        userRepository.save(testDataBuilder.createUnverifiedUser());

        LoginRequest request = new LoginRequest();
        request.setEmail(VALID_EMAIL);
        request.setPassword(VALID_PASSWORD);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Email not verified"));
    }

    @Test
    void login_WithWrongPassword_ShouldReturn401() throws Exception {
        // Arrange
        userRepository.save(testDataBuilder.createVerifiedUser());

        LoginRequest request = new LoginRequest();
        request.setEmail(VALID_EMAIL);
        request.setPassword("WrongPass123!@#");

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    void login_WithNonExistentEmail_ShouldReturn401() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("nonexistent@example.com");
        request.setPassword(VALID_PASSWORD);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }
}