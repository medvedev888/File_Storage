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
}
