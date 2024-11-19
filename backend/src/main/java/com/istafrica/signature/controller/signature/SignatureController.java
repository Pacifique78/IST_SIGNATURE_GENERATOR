package com.istafrica.signature.controller.signature;

import com.istafrica.signature.dto.signature.SignatureResponse;
import com.istafrica.signature.service.signature.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signature")
@RequiredArgsConstructor
public class SignatureController {
    private final SignatureService signatureService;

    @GetMapping("/generate")
    public ResponseEntity<SignatureResponse> generateSignature(Authentication authentication) {
        return ResponseEntity.ok(signatureService.generateSignature(authentication.getName()));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleSignatureUpdate() {
        // Implementation for handling signature updates
        return ResponseEntity.ok().build();
    }
}