package com.istafrica.signature.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.istafrica.signature.domain.entity.User;
import com.istafrica.signature.repository.CompanyRepository;
import com.istafrica.signature.repository.UserRepository;
import com.istafrica.signature.testutils.TestDataBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class AdminTestBase {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected CompanyRepository companyRepository;

    @Autowired
    protected TestDataBuilder testDataBuilder;

    protected User adminUser;
    protected User regularUser;
    protected String adminToken;
    protected String userToken;

    @BeforeEach
    void setUp() throws Exception {
        // Clean up
        userRepository.deleteAll();
        companyRepository.deleteAll();
        
        // Create and save admin user
        adminUser = testDataBuilder.createAdminUser();
        adminUser = userRepository.save(adminUser);
        adminToken = testDataBuilder.getAuthTokenForUser(adminUser, mockMvc, objectMapper, "Admin123!@#");
        
        // Create and save regular user
        regularUser = testDataBuilder.createVerifiedUser();
        regularUser = userRepository.save(regularUser);
        userToken = testDataBuilder.getAuthTokenForUser(regularUser, mockMvc, objectMapper, "Test123!@#");
    }
}