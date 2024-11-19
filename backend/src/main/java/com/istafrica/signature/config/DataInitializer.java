// src/main/java/com/istafrica/signature/config/DataInitializer.java

package com.istafrica.signature.config;

import com.istafrica.signature.domain.entity.Company;
import com.istafrica.signature.domain.entity.User;
import com.istafrica.signature.domain.enums.UserRole;
import com.istafrica.signature.repository.CompanyRepository;
import com.istafrica.signature.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:admin@istafrica.com}")
    private String adminEmail;

    @Value("${app.admin.password:Admin123!@#}")
    private String adminPassword;

    @Value("${app.admin.name:Admin User}")
    private String adminName;

    @Override
    @Transactional
    public void run(String... args) {
        initializeCompany();
        initializeAdminUser();
    }

    private void initializeCompany() {
        if (companyRepository.count() == 0) {
            log.info("Initializing default company data");
            
            Company company = Company.builder()
                    .name("IST Africa")
                    .missionStatement("Empowering Africa through innovative technology solutions")
                    .address("KG 345 st, Peace avenue")
                    .website("https://www.ist-africa.com")
                    .build();

            companyRepository.save(company);
            log.info("Default company data initialized successfully");
        } else {
            log.info("Company data already exists, skipping initialization");
        }
    }

    private void initializeAdminUser() {
        if (!userRepository.existsByEmail(adminEmail)) {
            log.info("Initializing admin user");
            
            User adminUser = User.builder()
                    .name(adminName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(UserRole.ADMIN)
                    .title("ADMINISTRATOR")
                    .phoneNumber("+250786758678")
                    .verified(true)
                    .build();

            userRepository.save(adminUser);
            log.info("Admin user initialized successfully");
        } else {
            log.info("Admin user already exists, skipping initialization");
        }
    }
}