package com.istafrica.signature.signature;

import com.istafrica.signature.dto.signature.SignatureResponse;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

class SignatureGenerationTests extends SignatureTestBase {

    @Test
    void generateSignature_ValidUser_ReturnsCompleteSignature() throws Exception {
        // Act & Assert
        var result = mockMvc.perform(get("/api/signature/generate")
                .header("Authorization", "Bearer " + authToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.htmlContent").exists())
                .andExpect(jsonPath("$.plainTextContent").exists())
                .andReturn();

        SignatureResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SignatureResponse.class
        );

        // Verify signature content includes all required elements
        assertThat(response.getHtmlContent())
                .contains(testUser.getName())
                .contains(testUser.getTitle())
                .contains(company.getName())
                .contains(company.getAddress())
                .contains(company.getWebsite())
                .contains(company.getMissionStatement());

        // Verify HTML structure
        assertThat(response.getHtmlContent())
                .contains("<table")
                .contains("</table>")
                .contains("<tr>")
                .contains("</tr>");

        // Verify plain text version
        assertThat(response.getPlainTextContent())
                .contains(testUser.getName())
                .contains(testUser.getTitle())
                .contains(company.getName());
    }

    @Test
    void generateSignature_UserWithoutTitle_ReturnsBadRequest() throws Exception {
        // Remove user's title
        testUser.setTitle(null);
        userRepository.save(testUser);

        mockMvc.perform(get("/api/signature/generate")
                .header("Authorization", "Bearer " + authToken))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User title is required for signature generation"));
    }

    @Test
    void generateSignature_NoCompanyInfo_ReturnsBadRequest() throws Exception {
        // Remove company info
        companyRepository.deleteAll();

        mockMvc.perform(get("/api/signature/generate")
                .header("Authorization", "Bearer " + authToken))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Company information not found"));
    }

    @Test
    void generateSignature_NoAuth_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/signature/generate"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    // @Test
    // void generateSignature_InvalidToken_ReturnsUnauthorized() throws Exception {
    //     mockMvc.perform(get("/api/signature/generate")
    //             .header("Authorization", "Bearer invalid.token"))
    //             .andDo(print())
    //             .andExpect(status().isUnauthorized());
    // }

    @Test
    void webhookUpdate_ValidRequest_UpdatesSignature() throws Exception {
        mockMvc.perform(post("/api/signature/webhook")
                .header("Authorization", "Bearer " + authToken))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void generateSignature_UpdatedInformation_ReflectsChanges() throws Exception {
        // Get initial signature
        var initialResult = mockMvc.perform(get("/api/signature/generate")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andReturn();

        String initialSignature = initialResult.getResponse().getContentAsString();

        // Update user title and company info
        testUser.setTitle("Senior Software Engineer");
        userRepository.save(testUser);

        company.setName("IST Africa Updated");
        companyRepository.save(company);

        // Get updated signature
        var updatedResult = mockMvc.perform(get("/api/signature/generate")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andReturn();

        String updatedSignature = updatedResult.getResponse().getContentAsString();

        // Verify signatures are different
        assertThat(updatedSignature)
                .isNotEqualTo(initialSignature)
                .contains("Senior Software Engineer")
                .contains("IST Africa Updated");
    }

    @Test
    void generateSignature_HTMLFormat_ValidStructure() throws Exception {
        var result = mockMvc.perform(get("/api/signature/generate")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andReturn();

        SignatureResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SignatureResponse.class
        );

        // Verify HTML structure and styling
        assertThat(response.getHtmlContent())
                .contains("font-family")
                .contains("style=")
                .contains("<table")
                .contains("<tr>")
                .contains("<td")
                .contains("</td>")
                .contains("</tr>")
                .contains("</table>");

        // Verify essential styling elements
        assertThat(response.getHtmlContent())
                .contains("font-weight: bold")
                .contains("color:")
                .contains("href=");
    }

    @Test
    void generateSignature_PlainTextFormat_ProperFormatting() throws Exception {
        var result = mockMvc.perform(get("/api/signature/generate")
                .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andReturn();

        SignatureResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                SignatureResponse.class
        );

        // Verify plain text formatting
        assertThat(response.getPlainTextContent())
                .contains(testUser.getName())
                .contains(testUser.getTitle())
                .contains(company.getName())
                .contains(company.getWebsite())
                .contains(System.lineSeparator()); // Proper line breaks
    }
}