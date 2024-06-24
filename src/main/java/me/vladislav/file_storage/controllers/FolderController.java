package me.vladislav.file_storage.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.dto.folder.FolderCreateDTO;
import me.vladislav.file_storage.dto.folder.FolderDeleteDTO;
import me.vladislav.file_storage.exceptions.folder.FolderCreationException;
import me.vladislav.file_storage.exceptions.folder.FolderDeletionException;
import me.vladislav.file_storage.services.MinioService;
import me.vladislav.file_storage.services.UserService;
import me.vladislav.file_storage.utils.PathUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/folder")
@RequiredArgsConstructor
public class FolderController {

    private final UserService userService;
    private final MinioService minioService;

    @PostMapping
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

            folderCreateDTO.setRootFolderPath(PathUtils.getRootPath(folderCreateDTO.getRootFolderPath(), currentUser.getId()));

            minioService.createFolder(folderCreateDTO, false);
        }
        redirectAttributes.addFlashAttribute("successMessage", "Folder created successfully");
        return new RedirectView("/?path=" + PathUtils.getPathWithoutRootUserFolder(folderCreateDTO.getRootFolderPath()));
    }

    @DeleteMapping
    public RedirectView deleteFolder(
            @AuthenticationPrincipal User user,
            @ModelAttribute("folderDeleteDTO") @Valid FolderDeleteDTO folderDeleteDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError("folderName") != null ?
                    bindingResult.getFieldError("folderName").getDefaultMessage() :
                    "Invalid folder name";

            throw new FolderDeletionException(errorMessage);
        } else {
            me.vladislav.file_storage.models.User currentUser = userService.getUserByLogin(user.getUsername());

            folderDeleteDTO.setRootFolderPath(PathUtils.getRootPath(folderDeleteDTO.getRootFolderPath(), currentUser.getId()));

            minioService.deleteFolder(folderDeleteDTO, currentUser.getId());
        }
        redirectAttributes.addFlashAttribute("successMessage", "Folder deleted successfully");
        return new RedirectView("/?path=" + PathUtils.getPathWithoutRootUserFolder(folderDeleteDTO.getRootFolderPath()));
    }
}

