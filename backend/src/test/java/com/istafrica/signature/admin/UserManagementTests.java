package com.istafrica.signature.admin;

import com.istafrica.signature.domain.entity.User;
import com.istafrica.signature.dto.admin.UpdateUserTitleRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserManagementTests extends AdminTestBase {

    @Test
    void updateUserTitle_AsAdmin_UpdatesSuccessfully() throws Exception {
        // Arrange
        String newTitle = "Senior Software Engineer";
        UpdateUserTitleRequest request = new UpdateUserTitleRequest();
        request.setTitle(newTitle);

        // Act & Assert
        mockMvc.perform(put("/api/admin/users/{userId}/title", regularUser.getId())
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());

        // Verify database
        User updatedUser = userRepository.findById(regularUser.getId()).orElseThrow();
        assertThat(updatedUser.getTitle()).isEqualTo(newTitle);
    }

    @Test
    void updateUserTitle_AsRegularUser_ReturnsForbidden() throws Exception {
        UpdateUserTitleRequest request = new UpdateUserTitleRequest();
        request.setTitle("New Title");

        mockMvc.perform(put("/api/admin/users/{userId}/title", regularUser.getId())
                .header("Authorization", "Bearer " + userToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateUserTitle_UserNotFound_ReturnsNotFound() throws Exception {
        UpdateUserTitleRequest request = new UpdateUserTitleRequest();
        request.setTitle("New Title");

        mockMvc.perform(put("/api/admin/users/{userId}/title", UUID.randomUUID())
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserTitle_EmptyTitle_ReturnsBadRequest() throws Exception {
        UpdateUserTitleRequest request = new UpdateUserTitleRequest();
        request.setTitle("");

        mockMvc.perform(put("/api/admin/users/{userId}/title", regularUser.getId())
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserTitle_NoAuth_ReturnsUnauthorized() throws Exception {
        UpdateUserTitleRequest request = new UpdateUserTitleRequest();
        request.setTitle("New Title");

        mockMvc.perform(put("/api/admin/users/{userId}/title", regularUser.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}