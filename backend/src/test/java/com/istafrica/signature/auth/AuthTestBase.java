package com.istafrica.signature.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
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
public abstract class AuthTestBase {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected TestDataBuilder testDataBuilder;

    @BeforeEach
    void cleanup() {
        userRepository.deleteAll();
    }

    protected static final String VALID_NAME = "Test User";
    protected static final String VALID_EMAIL = "test@example.com";
    protected static final String VALID_PASSWORD = "Test123!@#";
    protected static final String VALID_PHONE = "+1234567890";
}