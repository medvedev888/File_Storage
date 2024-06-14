package me.vladislav.file_storage.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.dto.UserDTO;
import me.vladislav.file_storage.dto.folder.FolderCreateDTO;
import me.vladislav.file_storage.services.FolderService;
import me.vladislav.file_storage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final FolderService folderService;

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
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            return "auth/registration";
        } else {
            userService.registerNewUserAccount(userDTO);
            try {
                request.login(userDTO.getLogin(), userDTO.getPassword());

                folderService.createFolder(new FolderCreateDTO("/", "user-" + userService.getUserByLogin(userDTO.getLogin()).getId() + "-files"), true);
            } catch (ServletException e) {
                model.addAttribute("loginError", "Login failed. Please check your credentials.");
                return "auth/authorization";
            }
            redirectAttributes.addFlashAttribute("successMessage", "You have successfully registered and logged in.");
            return "redirect:/";
        }
    }

}
