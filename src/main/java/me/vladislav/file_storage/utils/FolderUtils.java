package me.vladislav.file_storage.utils;

import java.util.Arrays;
import java.util.List;

public class FolderUtils {

    public static String getNameOfCurrentFolderByPath(String path) {
        StringBuffer stringBuffer = new StringBuffer(path);

        int k = 1;
        for (int i = stringBuffer.length() - 1; i >= 0; i--) {
            if (stringBuffer.charAt(i) == '/' && k == 0) {
                return stringBuffer.substring(i + 1, stringBuffer.length());
            } else if (stringBuffer.charAt(i) == '/') {
                k--;
            }

        }
        String result = stringBuffer.toString();
        return result.endsWith("/") ? result : result + "/";
    }

    public static String getOwnerFolder(String path, String targetName, boolean isNameRepeat, int numberOfDesiredFolder) {
        path = PathUtils.getValidPath(path);
        if (path.isEmpty()) {
            throw new IllegalArgumentException("The path cannot be null or empty.");
        }

        targetName = targetName.endsWith("/") ? targetName.substring(0, targetName.length() - 1) : targetName;

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
        throw new IllegalArgumentException("Invalid input.");
    }

}
