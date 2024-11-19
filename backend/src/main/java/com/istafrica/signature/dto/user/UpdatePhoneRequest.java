package com.istafrica.signature.dto.user;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdatePhoneRequest {
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phoneNumber;
}