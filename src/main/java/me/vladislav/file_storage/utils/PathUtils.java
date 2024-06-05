package me.vladislav.file_storage.utils;

public class PathUtils {

    public static String getRootPath(String path, Long userID){
        StringBuffer stringBuffer = new StringBuffer(getPathToRootUserFolder(userID) + path.strip());
        for(int i = 0; i < stringBuffer.length() - 1; i++){
            if(stringBuffer.charAt(i) == '/' && stringBuffer.charAt(i + 1) == '/') {
                stringBuffer.delete(i, i + 1);
                i--;
            }
        }
        return stringBuffer.toString().strip();
    }

    public static String getPathToRootUserFolder(Long userID){
        return "/user-" + userID + "-files/";
    }

}
