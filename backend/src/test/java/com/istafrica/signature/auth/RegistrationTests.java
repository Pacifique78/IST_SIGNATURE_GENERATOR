package com.istafrica.signature.auth;

import com.istafrica.signature.domain.entity.User;
import com.istafrica.signature.dto.auth.SignupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

class RegistrationTests extends AuthTestBase {

    @Test
    void signup_WithValidData_ShouldSucceed() throws Exception {
        // Arrange
        SignupRequest request = new SignupRequest();
        request.setName(VALID_NAME);
        request.setEmail(VALID_EMAIL);
        request.setPassword(VALID_PASSWORD);
        request.setPhoneNumber(VALID_PHONE);

        // Act & Assert
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(VALID_EMAIL))
                .andExpect(jsonPath("$.message").value("User registered successfully. Please check your email for verification."));

        // Verify user in database
        User savedUser = userRepository.findByEmail(VALID_EMAIL).orElseThrow();
        assertThat(savedUser.getName()).isEqualTo(VALID_NAME);
        assertThat(savedUser.getPhoneNumber()).isEqualTo(VALID_PHONE);
        assertThat(savedUser.isVerified()).isFalse();
    }

    @Test
    void signup_WithInvalidEmail_ShouldFail() throws Exception {
        // Arrange
        SignupRequest request = new SignupRequest();
        request.setName(VALID_NAME);
        request.setEmail("invalid-email");
        request.setPassword(VALID_PASSWORD);

        // Act & Assert
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.email").exists());
    }

    @Test
    void signup_WithWeakPassword_ShouldFail() throws Exception {
        // Arrange
        SignupRequest request = new SignupRequest();
        request.setName(VALID_NAME);
        request.setEmail(VALID_EMAIL);
        request.setPassword("weak");

        // Act & Assert
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.password").exists());
    }

    @Test
    void signup_WithExistingEmail_ShouldFail() throws Exception {
        // Arrange
        userRepository.save(testDataBuilder.createUnverifiedUser());

        SignupRequest request = new SignupRequest();
        request.setName(VALID_NAME);
        request.setEmail(VALID_EMAIL);
        request.setPassword(VALID_PASSWORD);

        // Act & Assert
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Email already exists"));
    }
}