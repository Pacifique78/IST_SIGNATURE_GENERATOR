package com.istafrica.signature.signature;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.istafrica.signature.domain.entity.Company;
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
public abstract class SignatureTestBase {

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

    protected User testUser;
    protected Company company;
    protected String authToken;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        companyRepository.deleteAll();

        // Create company
        company = Company.builder()
                .name("IST Africa")
                .missionStatement("Empowering through technology")
                .address("123 Tech Street")
                .website("https://www.ist-africa.com")
                .build();
        company = companyRepository.save(company);

        // Create verified user with title
        testUser = testDataBuilder.createVerifiedUser();
        testUser.setTitle("Software Engineer"); // Set title for signature
        testUser = userRepository.save(testUser);
        
        // Get auth token
        authToken = testDataBuilder.getAuthTokenForUser(testUser, mockMvc, objectMapper, "Test123!@#");
    }
}