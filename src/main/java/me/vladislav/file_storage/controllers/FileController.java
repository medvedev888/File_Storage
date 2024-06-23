package me.vladislav.file_storage.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.dto.file.FileUploadDTO;
import me.vladislav.file_storage.exceptions.file.FileUploadException;
import me.vladislav.file_storage.services.MinioService;
import me.vladislav.file_storage.services.UserService;
import me.vladislav.file_storage.utils.PathUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final MinioService minioService;
    private final UserService userService;

    @PostMapping
    public RedirectView uploadFile(
            @AuthenticationPrincipal User user,
            @ModelAttribute("fileUploadDTO") @Valid FileUploadDTO fileUploadDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            // TODO: need to change this block when will do validation in FileUploadDTO
//            String errorMessage = bindingResult.getFieldError("folderName") != null ?
//                    bindingResult.getFieldError("folderName").getDefaultMessage() :
//                    "Invalid folder name";

            throw new FileUploadException(""/*errorMessage*/);
        } else {
            me.vladislav.file_storage.models.User currentUser = userService.getUserByLogin(user.getUsername());

            fileUploadDTO.setRootFolderPath(PathUtils.getRootPath(fileUploadDTO.getRootFolderPath(), currentUser.getId()));

            minioService.uploadFile(fileUploadDTO);

            redirectAttributes.addFlashAttribute("successMessage", "File uploaded successfully");
            return new RedirectView("/?path=" + PathUtils.getPathWithoutRootUserFolder(fileUploadDTO.getRootFolderPath()));
        }

    }

}
