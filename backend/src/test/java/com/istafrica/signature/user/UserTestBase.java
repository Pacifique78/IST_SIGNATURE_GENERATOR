package com.istafrica.signature.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.istafrica.signature.domain.entity.User;
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
public abstract class UserTestBase {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TestDataBuilder testDataBuilder;

    protected User testUser;
    protected String authToken;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        
        // Create and save verified test user
        testUser = testDataBuilder.createVerifiedUser();
        testUser = userRepository.save(testUser);
        
        // Get auth token
        authToken = testDataBuilder.getAuthTokenForUser(testUser, mockMvc, objectMapper, "Test123!@#");
    }
}