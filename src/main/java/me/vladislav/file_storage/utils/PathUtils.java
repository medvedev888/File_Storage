package me.vladislav.file_storage.utils;

public class PathUtils {

    /**
     * @param path                  path containing {@code currentObjectName}
     * @param currentObjectName     name of current object
     * @param isNameRepeat          a boolean flag indicating whether a path can have folders with the same name
     * @param numberOfDesiredFolder position of desired folder, should be greater than zero, if {@code isNameRepeat=true}
     * @return valid path (using {@link #getValidPath(String path)}) without {@code currentFolderName}; empty string,
     * if path is empty
     */
    public static String getPathWithoutCurrentObject(String path, String currentObjectName, boolean isNameRepeat, Long numberOfDesiredFolder) {
        path = getValidPath(path);
        if (path.isEmpty()) {
            return "";
        }

        if (isNameRepeat) {
            String[] parts = path.split("/");
            StringBuffer result = new StringBuffer();
            for (String part : parts) {
                if ((part).equals(currentObjectName)) {
                    numberOfDesiredFolder--;
                }
                result.append(part).append('/');
                if (numberOfDesiredFolder == 0) {
                    return result.substring(0, result.toString().lastIndexOf(currentObjectName));
                }
            }
        }
        return path.substring(0, path.indexOf(currentObjectName));
    }

    /**
     * @param path   any path
     * @param userID id of user
     * @return valid path (using {@link #getValidPath(String path)}) with prefix (user-{@code userID}-files)
     */
    public static String getRootPath(String path, Long userID) {
        return getValidPath(getPathWithRootUserFolder(userID) + path);
    }

    /**
     * @param path path containing root user folder
     * @return valid path (using {@link #getValidPath(String path)}) without prefix (user-{@code userID}-files)
     */
    public static String getPathWithoutRootUserFolder(String path) {
        path = getValidPath(path);
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                return path.substring(i + 1);
            }
        }
        return path;
    }

    /**
     * @param userID id of user
     * @return string of type (user-{@code userID}-files)
     */
    public static String getPathWithRootUserFolder(Long userID) {
        return "user-" + userID + "-files/";
    }

    /**
     * @param path any path
     * @return valid path (without whitespace characters and duplicate characters {@code /})
     */
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
