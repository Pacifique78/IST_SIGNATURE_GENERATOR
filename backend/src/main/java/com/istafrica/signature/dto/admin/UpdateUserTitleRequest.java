package com.istafrica.signature.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateUserTitleRequest {
    @NotBlank(message = "Title is required")
    private String title;
}