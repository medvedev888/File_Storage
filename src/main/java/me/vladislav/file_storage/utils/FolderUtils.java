package me.vladislav.file_storage.utils;

import java.util.Arrays;
import java.util.List;

public class FolderUtils {

    /**
     * @param path any path
     * @return the number of repetitions of the last folder name
     */
    public static Long getTheNumberOfTheDuplicateFolderNameByPath(String path) {
        path = PathUtils.getValidPath(path);

        String[] parts = path.split("/");

        return Arrays.stream(parts).filter((x) -> x.equals(parts[parts.length - 1])).count();
    }

}
