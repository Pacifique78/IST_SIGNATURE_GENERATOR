package com.istafrica.signature.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailVerificationResponse {
    private String message;
    private String email;
}