package com.istafrica.signature.dto.user;

import lombok.Data;

import java.util.UUID;

import lombok.Builder;

@Data
@Builder
public class CompanyResponse {
    private UUID id;
    private String name;
    private String missionStatement;
    private String address;
    private String website;
}