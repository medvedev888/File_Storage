package me.vladislav.file_storage.controllers;

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
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        } else {
            userService.registerNewUserAccount(userDTO);
            return "redirect:home";
        }

    }
}
