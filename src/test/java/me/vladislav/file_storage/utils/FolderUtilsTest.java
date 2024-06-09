package me.vladislav.file_storage.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FolderUtilsTest {

    @Test
    void shouldReturnValidOwnerFolderByTargetName() {
        String path = "user-1-files/folder/folder/";
        String name = "folder/";

        String result = FolderUtils.getOwnerFolder(path, name);

        assertEquals("folder/", result);
    }

    @Test
    void shouldReturnValidNameOfCurrentFolderByPath() {
        String path = "user-1-files/folder/folder-2/";

        String result = FolderUtils.getNameOfCurrentFolderByPath(path);

        assertEquals("folder-2", result);
    }
}
