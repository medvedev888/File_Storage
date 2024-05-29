package me.vladislav.file_storage.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.dto.UserDTO;
import me.vladislav.file_storage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @GetMapping("/authorization")
    public String showAuthorizationPage() {
        return "auth/authorization";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new UserDTO());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String register(
            @ModelAttribute("user") @Valid UserDTO userDTO,
            BindingResult bindingResult,
            HttpServletRequest request,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        } else {
            userService.registerNewUserAccount(userDTO);
            try {
                request.login(userDTO.getLogin(), userDTO.getPassword());
            } catch (ServletException e) {
                model.addAttribute("loginError", "Login failed. Please check your credentials.");
                return "auth/authorization";
            }
            return "redirect:/";
        }
    }

}
