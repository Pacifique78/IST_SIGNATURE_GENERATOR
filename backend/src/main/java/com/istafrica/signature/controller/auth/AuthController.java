package com.istafrica.signature.controller.auth;

import com.istafrica.signature.dto.auth.*;
import com.istafrica.signature.service.auth.AuthService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<EmailVerificationResponse> verifyEmail(
            @Valid @RequestBody EmailVerificationRequest request) {
        return ResponseEntity.ok(authService.verifyEmail(request));
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Void> resendVerificationEmail(@RequestParam @Email String email) {
        authService.resendVerificationEmail(email);
        return ResponseEntity.ok().build();
    }
}