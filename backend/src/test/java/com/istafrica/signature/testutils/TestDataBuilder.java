package com.istafrica.signature.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.istafrica.signature.domain.entity.User;
import com.istafrica.signature.domain.enums.UserRole;
import com.istafrica.signature.dto.auth.LoginRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestDataBuilder {

    private final PasswordEncoder passwordEncoder;

    public User.UserBuilder createDefaultUserBuilder() {
        return User.builder()
                .name("Test User")
                .email("test@example.com")
                .password(passwordEncoder.encode("Test123!@#"))
                .phoneNumber("+1234567890")
                .role(UserRole.USER)
                .verified(false);
    }

    public User createVerifiedUser() {
        return createDefaultUserBuilder()
                .verified(true)
                .build();
    }

    public User createUnverifiedUser() {
        return createDefaultUserBuilder()
                .verified(false)
                .build();
    }

    public User createAdminUser() {
        return User.builder()
                .name("Admin User")
                .email("admin@example.com")
                .password(passwordEncoder.encode("Admin123!@#"))
                .phoneNumber("+1234567890")
                .role(UserRole.ADMIN)
                .verified(true)
                .build();
    }

    public String getAuthTokenForUser(User user, MockMvc mockMvc, ObjectMapper objectMapper, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword(password);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("token").asText();
    }
}