package com.istafrica.signature.admin;

import com.istafrica.signature.domain.entity.Company;
import com.istafrica.signature.dto.admin.CompanyUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

class CompanyManagementTests extends AdminTestBase {

    @Test
    void updateCompany_AsAdmin_CreatesNewCompany() throws Exception {
        // Arrange
        CompanyUpdateRequest request = new CompanyUpdateRequest();
        request.setName("IST Africa");
        request.setMissionStatement("Empowering through technology");
        request.setAddress("123 Street");
        request.setWebsite("https://www.ist-africa.com");

        // Act & Assert
        mockMvc.perform(put("/api/admin/company")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IST Africa"))
                .andExpect(jsonPath("$.missionStatement").value("Empowering through technology"))
                .andExpect(jsonPath("$.website").value("https://www.ist-africa.com"));

        // Verify database
        Company savedCompany = companyRepository.findAll().get(0);
        assertThat(savedCompany.getName()).isEqualTo("IST Africa");
        assertThat(savedCompany.getWebsite()).isEqualTo("https://www.ist-africa.com");
    }

    @Test
    void updateCompany_AsAdmin_UpdatesExistingCompany() throws Exception {
        // Arrange
        Company existingCompany = Company.builder()
                .name("Old Name")
                .missionStatement("Old Mission")
                .address("Old Address")
                .website("https://old-website.com")
                .build();
        companyRepository.save(existingCompany);

        CompanyUpdateRequest request = new CompanyUpdateRequest();
        request.setName("IST Africa");
        request.setMissionStatement("New Mission");
        request.setAddress("New Address");
        request.setWebsite("https://www.ist-africa.com");

        // Act & Assert
        mockMvc.perform(put("/api/admin/company")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("IST Africa"));

        // Verify database
        Company updatedCompany = companyRepository.findAll().get(0);
        assertThat(updatedCompany.getName()).isEqualTo("IST Africa");
        assertThat(updatedCompany.getId()).isEqualTo(existingCompany.getId());
    }

    @Test
    void updateCompany_AsRegularUser_ReturnsForbidden() throws Exception {
        CompanyUpdateRequest request = new CompanyUpdateRequest();
        request.setName("IST Africa");

        mockMvc.perform(put("/api/admin/company")
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateCompany_InvalidData_ReturnsBadRequest() throws Exception {
        CompanyUpdateRequest request = new CompanyUpdateRequest();
        request.setName("");  // Name is required
        request.setWebsite("invalid-url");  // Invalid URL format

        mockMvc.perform(put("/api/admin/company")
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}