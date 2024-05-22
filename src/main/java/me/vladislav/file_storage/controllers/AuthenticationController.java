package me.vladislav.file_storage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping("/authorization")
    public String showAuthorizationPage(){
        return "auth/authorization";
    }

    @GetMapping("/registration")
    public String showRegistrationPage(){
        return "auth/registration";
    }
}
