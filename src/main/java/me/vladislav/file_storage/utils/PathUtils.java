package me.vladislav.file_storage.utils;

public class PathUtils {

    public static String getRootPath(String path, Long userID){
        return getValidPath(getPathToRootUserFolder(userID) + path);
    }

    public static String getPathWithoutRootUserFolder(String path) {
        path = getValidPath(path);
        for(int i = 0; i < path.length(); i++) {
            if(path.charAt(i) == '/') {
                return path.substring(i + 1);
            }
        }
        return path;
    }

    public static String getPathToRootUserFolder(Long userID){
        return "user-" + userID + "-files/";
    }

    public static String getValidPath(String path) {
        StringBuffer stringBuffer = new StringBuffer(path.strip());
        for(int i = 0; i < stringBuffer.length() - 1; i++){
            if(stringBuffer.charAt(i) == '/' && stringBuffer.charAt(i + 1) == '/') {
                stringBuffer.delete(i, i + 1);
                i--;
            }
        }
        return stringBuffer.toString().strip();
    }

}
