package me.vladislav.file_storage.utils;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathUtilsTest {

    @Test
    void shouldReturnValidRootPathTest() {
        String path = "/////haha//";
        Long userID = 123L;

        String resultPath = PathUtils.getRootPath(path, userID);

        assertEquals("user-123-files/haha/", resultPath);

    }

    @Test
    void shouldReturnValidFolderNameWithoutPath() {
        String folderName = "user-123-files/folder-1/folder-2/";

        String resultFolderName = PathUtils.getPathWithoutRootUserFolder(folderName);

        assertEquals("folder-1/folder-2/", resultFolderName);
    }

    @Test
    void shouldReturnPathWithoutCurrentFolder() {
        String path = "user-1-files/folder-1/folder-1/folder-3/folder-1/";
        String currentFolderName1 = "folder-1";

        String result1 = PathUtils.getPathWithoutCurrentObject(path, currentFolderName1, true, 2L);
        String result2 = PathUtils.getPathWithoutCurrentObject(path, currentFolderName1, true, 1L);
        String result3 = PathUtils.getPathWithoutCurrentObject(path, currentFolderName1, true, 3L);

        assertEquals("user-1-files/folder-1/", result1);
        assertEquals("user-1-files/", result2);
        assertEquals("user-1-files/folder-1/folder-1/folder-3/", result3);
    }

}
