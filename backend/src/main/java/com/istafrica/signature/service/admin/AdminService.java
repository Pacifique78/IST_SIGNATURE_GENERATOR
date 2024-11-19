package com.istafrica.signature.service.admin;

import com.istafrica.signature.domain.entity.Company;
import com.istafrica.signature.domain.entity.User;
import com.istafrica.signature.dto.admin.CompanyUpdateRequest;
import com.istafrica.signature.dto.admin.UpdateUserTitleRequest;
import com.istafrica.signature.exception.CustomExceptions.ResourceNotFoundException;
import com.istafrica.signature.repository.CompanyRepository;
import com.istafrica.signature.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    @Transactional
    public Company updateCompany(CompanyUpdateRequest request) {
        Company company = companyRepository.findAll().stream().findFirst()
                .orElse(new Company());

        company.setName(request.getName());
        company.setMissionStatement(request.getMissionStatement());
        company.setAddress(request.getAddress());
        company.setWebsite(request.getWebsite());

        return companyRepository.save(company);
    }

    @Transactional
    public void updateUserTitle(UUID userId, UpdateUserTitleRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setTitle(request.getTitle());
        userRepository.save(user);
    }

    @Transactional
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}