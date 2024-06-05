package me.vladislav.file_storage.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.dto.FolderCreateDTO;
import me.vladislav.file_storage.services.FolderService;
import me.vladislav.file_storage.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class FolderController {

    private final UserService userService;
    private final FolderService folderService;

    @PostMapping("/folder")
    public String createFolder(
            @AuthenticationPrincipal User user,
            @ModelAttribute("folderCreateDTO") @Valid FolderCreateDTO folderCreateDTO,
            BindingResult bindingResult
            ) {

        if(bindingResult.hasErrors()){
            //TODO: need to add exception handling
        } else {
            me.vladislav.file_storage.models.User currentUser = userService.getUserByLogin(user.getUsername());

            folderService.createFolder("/user-" + currentUser.getId() + "-files/" + folderCreateDTO.getRootFolderPath(), folderCreateDTO.getNameOfNewFolder());
        }

        return "redirect:" + folderCreateDTO.getRootFolderPath();
    }
}

