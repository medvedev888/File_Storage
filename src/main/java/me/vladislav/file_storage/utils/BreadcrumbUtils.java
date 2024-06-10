package me.vladislav.file_storage.utils;

import java.util.*;

public class BreadcrumbUtils {

    public static Map<String, String> getMapOfLinksFromPath(String path) {
        Map<String, String> breadcrumbsMap = new LinkedHashMap<>();

        path = PathUtils.getPathWithoutRootUserFolder(path);

        List<String> listOfLinks = Arrays.stream(path.split("/")).toList();

        breadcrumbsMap.put("home", "/");

        for(String link : listOfLinks) {
            String pathForLink = PathUtils.getPathWithoutCurrentFolder(path, link) + link;
            if(!pathForLink.isBlank()) {
                breadcrumbsMap.put(link, PathUtils.getValidPath("/" + pathForLink + '/'));
            }
        }

        return breadcrumbsMap;
    }
}
