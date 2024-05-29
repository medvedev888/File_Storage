package me.vladislav.file_storage.controllers;

import me.vladislav.file_storage.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BindingResult bindingResult;

    @MockBean
    private UserService userService;

    @Test
    void shouldReturnRegistrationViewIfTheInputIsInvalid() throws Exception {
        String login = "1";
        String password = "1";

        mockMvc.perform(post("/registration")
                        .param("login", login)
                        .param("password", password))
                .andExpect(view().name("auth/registration"));

        assertNotEquals(null, bindingResult);
        verifyNoInteractions(userService);
    }
}
