package me.vladislav.file_storage.integration;

import me.vladislav.file_storage.models.User;
import me.vladislav.file_storage.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("dev")
public class UserRegistrationIntegrationTest {
    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer("postgres:14.11")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterAndSaveNewUser() throws Exception {

        String login = "maks_dubov";
        String password = "123123123";

        mockMvc.perform(post("/registration")
                        .param("login", login)
                        .param("password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        User user = userRepository.findUserByLogin(login).orElse(null);
        assertEquals(login, user.getLogin());
        assertTrue(passwordEncoder.matches(password, user.getPassword()));

    }

}

