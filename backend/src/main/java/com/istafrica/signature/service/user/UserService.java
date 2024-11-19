package com.istafrica.signature.service.user;

import com.istafrica.signature.domain.entity.Company;
import com.istafrica.signature.domain.entity.User;
import com.istafrica.signature.dto.user.UserProfileResponse;
import com.istafrica.signature.dto.user.CompanyResponse;
import com.istafrica.signature.dto.user.UpdatePhoneRequest;
import com.istafrica.signature.repository.CompanyRepository;
import com.istafrica.signature.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public UserProfileResponse getUserProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .title(user.getTitle())
                .role(user.getRole().toString())
                .verified(user.isVerified())
                .build();
    }

    public CompanyResponse getCompany() {
        Company company = companyRepository.findAll().stream().findFirst()
                .orElse(new Company());

        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .missionStatement(company.getMissionStatement())
                .address(company.getAddress())
                .website(company.getWebsite())
                .build();
    }

    @Transactional
    public void updatePhoneNumber(String email, UpdatePhoneRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);
    }
}