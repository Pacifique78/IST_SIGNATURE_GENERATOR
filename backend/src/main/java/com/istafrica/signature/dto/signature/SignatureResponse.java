package com.istafrica.signature.dto.signature;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignatureResponse {
    private String htmlContent;
    private String plainTextContent;
}