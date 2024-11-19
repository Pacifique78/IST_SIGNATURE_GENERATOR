package com.istafrica.signature.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CompanyUpdateRequest {
    @NotBlank(message = "Company name is required")
    private String name;
    
    private String missionStatement;
    private String address;
    
    @Pattern(
        regexp = "^(https?:\\/\\/)?([\\w\\-])+\\.{1}([a-zA-Z]{2,63})([\\/\\w-]*)*\\/?\\??([^#\\n\\r]*)?#?([^\\n\\r]*)$",
        message = "Invalid website URL"
    )
    private String website;
}