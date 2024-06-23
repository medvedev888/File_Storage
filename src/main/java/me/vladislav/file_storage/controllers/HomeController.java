package me.vladislav.file_storage.controllers;

import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.dto.MinioObjectDTO;
import me.vladislav.file_storage.dto.file.FileUploadDTO;
import me.vladislav.file_storage.dto.folder.FolderCreateDTO;
import me.vladislav.file_storage.dto.folder.FolderDeleteDTO;
import me.vladislav.file_storage.services.MinioService;
import me.vladislav.file_storage.services.UserService;
import me.vladislav.file_storage.utils.BreadcrumbUtils;
import me.vladislav.file_storage.utils.PathUtils;
import org.springframework.data.util.Pair;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/")
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MinioService minioService;
    private final UserService userService;

    @GetMapping
    public String showHomePage(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "path", required = false, defaultValue = "/") String path,
            Model model
    ) {
        model.addAttribute("currentPath", path.charAt(0) != '/' ? "/" + path : path);

        me.vladislav.file_storage.models.User currentUser = userService.getUserByLogin(user.getUsername());
        path = PathUtils.getRootPath(path, currentUser.getId());

        List<Pair<String, String>> breadcrumbsList = BreadcrumbUtils.getListOfBreadcrumbsFromPath(path);
        model.addAttribute("breadcrumbsList", breadcrumbsList);

        List<MinioObjectDTO> listOfMinioObject = minioService.getMinioObject(path, false).getListOfMinioObject();
        model.addAttribute("listOfMinioObject", listOfMinioObject);

        model.addAttribute("folderCreateDTO", new FolderCreateDTO());
        model.addAttribute("folderDeleteDTO", new FolderDeleteDTO());

        model.addAttribute("fileUploadDTO", new FileUploadDTO());

        return "home";
    }
}
