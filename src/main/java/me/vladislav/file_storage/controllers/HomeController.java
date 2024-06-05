package me.vladislav.file_storage.controllers;

import me.vladislav.file_storage.dto.FolderCreateDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class HomeController {
    @GetMapping
    public String showHomePage(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("folderCreateDTO", new FolderCreateDTO());
        return "home";
    }
}
