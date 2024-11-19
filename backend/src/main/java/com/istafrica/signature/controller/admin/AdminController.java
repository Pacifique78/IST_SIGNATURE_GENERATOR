package com.istafrica.signature.controller.admin;

import com.istafrica.signature.domain.entity.Company;
import com.istafrica.signature.domain.entity.User;
import com.istafrica.signature.dto.admin.CompanyUpdateRequest;
import com.istafrica.signature.dto.admin.UpdateUserTitleRequest;
import com.istafrica.signature.service.admin.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PutMapping("/company")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody CompanyUpdateRequest request) {
        return ResponseEntity.ok(adminService.updateCompany(request));
    }

    @PutMapping("/users/{userId}/title")
    public ResponseEntity<Void> updateUserTitle(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserTitleRequest request) {
        adminService.updateUserTitle(userId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(adminService.getUsers());
    }
}