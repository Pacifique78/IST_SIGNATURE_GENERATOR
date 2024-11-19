package com.istafrica.signature.user;

import com.istafrica.signature.domain.entity.User;
import com.istafrica.signature.dto.user.UpdatePhoneRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

class UserProfileTests extends UserTestBase {

    @Test
    void getProfile_AuthenticatedUser_ReturnsProfile() throws Exception {
        mockMvc.perform(get("/api/users/profile")
                .header("Authorization", "Bearer " + authToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testUser.getName()))
                .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(testUser.getPhoneNumber()))
                .andExpect(jsonPath("$.verified").value(true));
    }

    @Test
    void getProfile_NoAuth_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/profile"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    // @Test
    // void getProfile_InvalidToken_ReturnsUnauthorized() throws Exception {
    //     mockMvc.perform(get("/api/users/profile")
    //             .header("Authorization", "Bearer test.invalid.token"))
    //             .andDo(print())
    //             .andExpect(status().isForbidden());
    // }

    @Test
    void updatePhoneNumber_ValidNumber_UpdatesSuccessfully() throws Exception {
        String newPhone = "+9876543210";
        UpdatePhoneRequest request = new UpdatePhoneRequest();
        request.setPhoneNumber(newPhone);

        mockMvc.perform(put("/api/users/phone-number")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        // Verify database update
        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(updatedUser.getPhoneNumber()).isEqualTo(newPhone);
    }

    @Test
    void updatePhoneNumber_InvalidFormat_ReturnsBadRequest() throws Exception {
        UpdatePhoneRequest request = new UpdatePhoneRequest();
        request.setPhoneNumber("invalid-format");

        mockMvc.perform(put("/api/users/phone-number")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // Verify phone number wasn't updated
        User unchangedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(unchangedUser.getPhoneNumber()).isEqualTo(testUser.getPhoneNumber());
    }

    @Test
    void updatePhoneNumber_NoAuth_ReturnsUnauthorized() throws Exception {
        UpdatePhoneRequest request = new UpdatePhoneRequest();
        request.setPhoneNumber("+9876543210");

        mockMvc.perform(put("/api/users/phone-number")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void updatePhoneNumber_NullPhone_UpdatesSuccessfully() throws Exception {
        UpdatePhoneRequest request = new UpdatePhoneRequest();
        request.setPhoneNumber(null);

        mockMvc.perform(put("/api/users/phone-number")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        // Verify phone number was set to null
        User updatedUser = userRepository.findById(testUser.getId()).orElseThrow();
        assertThat(updatedUser.getPhoneNumber()).isNull();
    }

    @Test
    void updatePhoneNumber_EmptyPhone_ReturnsBadRequest() throws Exception {
        UpdatePhoneRequest request = new UpdatePhoneRequest();
        request.setPhoneNumber("");

        mockMvc.perform(put("/api/users/phone-number")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}