package me.vladislav.file_storage.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.dto.folder.FolderCreateDTO;
import me.vladislav.file_storage.exceptions.folders.FolderCreationException;
import me.vladislav.file_storage.services.FolderService;
import me.vladislav.file_storage.services.UserService;
import me.vladislav.file_storage.utils.PathUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequiredArgsConstructor
public class FolderController {

    private final UserService userService;
    private final FolderService folderService;

    @PostMapping("/folder")
    public RedirectView createFolder(
            @AuthenticationPrincipal User user,
            @ModelAttribute("folderCreateDTO") @Valid FolderCreateDTO folderCreateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError("nameOfNewFolder") != null ?
                    bindingResult.getFieldError("nameOfNewFolder").getDefaultMessage() :
                    "Invalid folder name";

            throw new FolderCreationException(errorMessage);
        } else {
            me.vladislav.file_storage.models.User currentUser = userService.getUserByLogin(user.getUsername());

            folderService.createFolder(PathUtils.getRootPath(folderCreateDTO.getRootFolderPath(), currentUser.getId()), folderCreateDTO.getNameOfNewFolder(), false);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Folder created successfully");
        return new RedirectView("/?path=" + PathUtils.getPathWithoutRootUserFolder(folderCreateDTO.getRootFolderPath()));
    }
}

