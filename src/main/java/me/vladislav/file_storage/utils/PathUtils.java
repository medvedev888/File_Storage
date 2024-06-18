package me.vladislav.file_storage.utils;

//TODO: разобраться с многопоточностью
public class PathUtils {

    public static String getPathWithoutCurrentFolder(String path, String currentFolderName, boolean isNameRepeat, int numberOfDesiredFolder) {
        path = getValidPath(path);
        if (path.isEmpty()) {
            return "";
        }

        currentFolderName = currentFolderName.endsWith("/") ? currentFolderName.substring(0, currentFolderName.length() - 1) : currentFolderName;

        if(isNameRepeat) {
            String[] parts = path.split("/");
            StringBuffer result = new StringBuffer();
            for (String part : parts) {
                if ((part).equals(currentFolderName)) {
                    numberOfDesiredFolder--;
                }
                result.append(part).append('/');
                if (numberOfDesiredFolder == 0) {
                    return result.substring(0, result.toString().lastIndexOf(currentFolderName));
                }
            }
        } else {
            return path.substring(0, path.indexOf(currentFolderName));
        }
        throw new IllegalArgumentException("Invalid input.");
    }

    public static String getRootPath(String path, Long userID) {
        return getValidPath(getPathWithRootUserFolder(userID) + path);
    }

    public static String getPathWithoutRootUserFolder(String path) {
        path = getValidPath(path);
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                return path.substring(i + 1);
            }
        }
        return path;
    }

    public static String getPathWithRootUserFolder(Long userID) {
        return "user-" + userID + "-files/";
    }

    public static String getValidPath(String path) {
        StringBuffer stringBuffer = new StringBuffer(path.strip());
        for (int i = 0; i < stringBuffer.length() - 1; i++) {
            if (stringBuffer.charAt(i) == '/' && stringBuffer.charAt(i + 1) == '/') {
                stringBuffer.delete(i, i + 1);
                i--;
            }
        }
        return stringBuffer.toString().strip();
    }

}
