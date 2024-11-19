package com.istafrica.signature.service.signature;

import com.istafrica.signature.domain.entity.Company;
import com.istafrica.signature.domain.entity.User;
import com.istafrica.signature.dto.signature.SignatureResponse;
import com.istafrica.signature.exception.CustomExceptions.ResourceNotFoundException;
import com.istafrica.signature.exception.CustomExceptions.ValidationException;
import com.istafrica.signature.repository.CompanyRepository;
import com.istafrica.signature.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignatureService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public SignatureResponse generateSignature(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Company company = companyRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Company information not found"));

        if (user.getTitle() == null || user.getTitle().isEmpty()) {
            throw new ValidationException("User title is required for signature generation");
        }

        String htmlContent = generateHtmlSignature(user, company);
        String plainTextContent = generatePlainTextSignature(user, company);

        return SignatureResponse.builder()
                .htmlContent(htmlContent)
                .plainTextContent(plainTextContent)
                .build();
    }

    private String generateHtmlSignature(User user, Company company) {
        return String.format("""
                <table style="font-family: Arial, sans-serif; color: #333333;">
                    <tr>
                        <td style="font-weight: bold; font-size: 16px;">%s</td>
                    </tr>
                    <tr>
                        <td style="font-weight: bold;">%s</td>
                    </tr>
                    <tr>
                        <td style="color: #666666;">%s</td>
                    </tr>
                    <tr>
                        <td style="color: #666666;">%s</td>
                    </tr>
                    <tr>
                        <td><a href="%s" style="color: #0066cc;">%s</a></td>
                    </tr>
                    <tr>
                        <td style="font-style: italic; color: #999999; padding-top: 10px;">%s</td>
                    </tr>
                </table>
                """,
                user.getName(),
                user.getTitle(),
                company.getName(),
                company.getAddress(),
                company.getWebsite(),
                company.getWebsite(),
                company.getMissionStatement());
    }

    private String generatePlainTextSignature(User user, Company company) {
        return String.format("""
                %s
                %s
                %s
                %s
                %s

                %s
                """,
                user.getName(),
                user.getTitle(),
                company.getName(),
                company.getAddress(),
                company.getWebsite(),
                company.getMissionStatement());
    }
}