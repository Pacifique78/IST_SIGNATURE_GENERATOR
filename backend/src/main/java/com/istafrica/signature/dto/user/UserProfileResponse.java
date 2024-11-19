package com.istafrica.signature.dto.user;

import lombok.Data;

import java.util.UUID;

import lombok.Builder;

@Data
@Builder
public class UserProfileResponse {
    private UUID id;
    private String name;
    private String email;
    private String phoneNumber;
    private String title;
    private String role;
    private boolean verified;
}