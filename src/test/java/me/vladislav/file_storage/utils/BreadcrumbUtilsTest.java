package me.vladislav.file_storage.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BreadcrumbUtilsTest {

    @Test
    void shouldReturnPathWithoutCurrentFolder() {
        String path = "user-1-files/folder-1/folder-2/folder-3/";
        String currentFolderName1 = "folder-2/";
        String currentFolderName2 = "folder-3";

        String result1 = PathUtils.getPathWithoutCurrentFolder(path, currentFolderName1);
        String result2 = PathUtils.getPathWithoutCurrentFolder(path, currentFolderName2);

        assertEquals("user-1-files/folder-1/", result1);
        assertEquals("user-1-files/folder-1/folder-2/", result2);
    }

}
