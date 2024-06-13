package me.vladislav.file_storage.controllers;

import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.dto.folder.FolderCreateDTO;
import me.vladislav.file_storage.dto.MinioObjectDTO;
import me.vladislav.file_storage.services.FolderService;
import me.vladislav.file_storage.services.UserService;
import me.vladislav.file_storage.utils.BreadcrumbUtils;
import me.vladislav.file_storage.utils.PathUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@RequestMapping("/")
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final FolderService folderService;
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

        Map<String, String> breadcrumbsMap = BreadcrumbUtils.getMapOfLinksFromPath(path);
        model.addAttribute("breadcrumbsMap", breadcrumbsMap);

        List<MinioObjectDTO> listOfFolders = folderService.getFolders(path, false);
        model.addAttribute("listOfFolders", listOfFolders);

        model.addAttribute("folderCreateDTO", new FolderCreateDTO());
        return "home";
    }
}
