package me.vladislav.file_storage.utils;

import java.util.Arrays;
import java.util.List;

public class MinioObjectUtils {
    public static String getNameOfCurrentObjectByPath(String path) {
        List<String> parts = Arrays.stream(path.split("/")).toList();
        String res = parts.get(parts.size() - 1);
        return path.endsWith("/") ? res + "/" : res;
    }

    public static String getObjectNameToDisplay(String objectName) {
        if (objectName.endsWith("/")) {
            return objectName.substring(0, objectName.length() - 1);
        }
        return objectName;
    }

    public static String getOwnerFolder(String path, String targetName, boolean isNameRepeat, Long numberOfDesiredFolder) {
        path = PathUtils.getValidPath(path);
        if (path.isEmpty()) {
            return "";
        }

        List<String> parts = Arrays.stream(path.split("/")).toList();
        int counterOfParts = 0;
        for (String part : parts) {
            if ((part).equals(targetName)) {
                numberOfDesiredFolder--;
                if (!isNameRepeat || numberOfDesiredFolder == 0) {
                    return parts.get(counterOfParts - 1) + "/";
                }
            }
            counterOfParts++;
        }
        return parts.get(counterOfParts - 1) + "/";
    }

}
