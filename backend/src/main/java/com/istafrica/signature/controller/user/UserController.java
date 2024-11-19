package com.istafrica.signature.controller.user;

import com.istafrica.signature.dto.user.UserProfileResponse;
import com.istafrica.signature.dto.user.CompanyResponse;
import com.istafrica.signature.dto.user.UpdatePhoneRequest;
import com.istafrica.signature.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
        return ResponseEntity.ok(userService.getUserProfile(authentication.getName()));
    }

    @GetMapping("/company")
    public ResponseEntity<CompanyResponse> getCompany(Authentication authentication) {
        return ResponseEntity.ok(userService.getCompany());
    }

    @PutMapping("/phone-number")
    public ResponseEntity<Void> updatePhoneNumber(
            Authentication authentication,
            @Valid @RequestBody UpdatePhoneRequest request) {
        userService.updatePhoneNumber(authentication.getName(), request);
        return ResponseEntity.ok().build();
    }
}