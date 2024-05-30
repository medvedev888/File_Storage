package me.vladislav.file_storage.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import me.vladislav.file_storage.dto.UserDTO;
import me.vladislav.file_storage.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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

    @MockBean
    private HttpServletRequest httpServletRequest;

    @MockBean
    private Model model;

    @Autowired
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnARegistrationViewIfTheInputIsInvalid() throws Exception {
        String login = "1";
        String password = "1";

        when(bindingResult.hasErrors()).thenReturn(true);

        mockMvc.perform(post("/registration")
                        .param("login", login)
                        .param("password", password))
                .andExpect(view().name("auth/registration"));

        assertNotEquals(null, bindingResult);
        verifyNoInteractions(userService);
    }

    @Test
    void shouldReturnAnAuthorizationViewIfAnServletExceptionOccurs() throws ServletException {
        String login = "maksim_nabokov";
        String password = "123123123";
        UserDTO userDTO = new UserDTO(login, password);

        doThrow(new ServletException("Simulated ServletException")).when(httpServletRequest).login(anyString(), anyString());
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(userService).registerNewUserAccount(any());

        String viewName = authenticationController.register(userDTO, bindingResult, httpServletRequest, model);

        assertEquals("auth/authorization", viewName);
        verify(model).addAttribute("loginError", "Login failed. Please check your credentials.");
        verify(httpServletRequest).login(login, password);
    }
}
