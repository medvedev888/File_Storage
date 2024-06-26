package me.vladislav.file_storage.utils;

import org.springframework.data.util.Pair;

import java.util.*;

public class BreadcrumbUtils {

    /**
     * @param path any path
     * @return {@code List<Pair<String, String>>} where the first string is the name of the link to
     * display and the second is string - valid path for moving between directories
     */
    public static List<Pair<String, String>> getListOfBreadcrumbsFromPath(String path) {
        List<Pair<String, String>> breadcrumbsList = new LinkedList<>();
        Map<String, Long> linksMap = new HashMap<>();

        path = PathUtils.getPathWithoutRootUserFolder(path);

        int k = 1;
        List<String> listOfLinks = Arrays.stream(path.split("/")).toList();
        for (String link : listOfLinks) {
            linksMap.put(link, linksMap.getOrDefault(link, 0L) + 1);
            String pathForLink = PathUtils.getPathWithoutCurrentObject(path, link, true, linksMap.get(link)) + link;
            if (!pathForLink.isBlank()) {
                breadcrumbsList.add(Pair.of(link, PathUtils.getValidPath("/" + pathForLink + '/')));
            }
            k++;
        }

        return breadcrumbsList;
    }
}
