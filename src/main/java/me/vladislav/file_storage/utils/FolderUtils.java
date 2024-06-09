package me.vladislav.file_storage.utils;

import java.util.Arrays;
import java.util.List;

public class FolderUtils {

    public static String getNameOfCurrentFolderByPath(String path) {
        StringBuffer stringBuffer = new StringBuffer(path);

        int k = 1;
        for(int i = stringBuffer.length() - 1; i >= 0; i--) {
            if(stringBuffer.charAt(i) == '/' && k == 0) {
                return stringBuffer.substring(i + 1, stringBuffer.length() - 1);
            } else if(stringBuffer.charAt(i) == '/') {
                k--;
            }

        }
        return stringBuffer.toString();
    }

    public static String getOwnerFolder(String path, String targetName) {
        List<String> listOfFolders;

        listOfFolders = Arrays.stream(path.split("/")).toList();

        int i = 0;
        for(String s : listOfFolders) {
            if(s.equals(targetName)) {
                return listOfFolders.get(i) + "/";
            }
            i++;
        }
        // TODO: may need to throw an exception: path don't have a owner and etc;
        return listOfFolders.get(listOfFolders.size() - 1) + "/";
    }

}
