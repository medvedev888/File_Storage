package me.vladislav.file_storage.utils;

public class PathUtils {

    public static String getRootPath(String path, Long userID) {
        return getValidPath(getPathWithRootUserFolder(userID) + path);
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
