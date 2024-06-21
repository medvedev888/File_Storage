package me.vladislav.file_storage.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FolderUtilsTest {

    @Test
    void shouldReturnValidOwnerFolderByTargetName() {
        String path1 = "user-1-files/folder/folder/";
        String name1 = "folder/";
        String path2 = "user-1-files/folder/haha/";
        String name2 = "folder";
        String name3 = "haha/";

        String result1 = FolderUtils.getOwnerFolder(path1, name1, true, 1L);
        String result2 = FolderUtils.getOwnerFolder(path2, name2, false, 123L);
        String result3 = FolderUtils.getOwnerFolder(path2, name3, false, 123L);

        assertEquals("user-1-files/", result1);
        assertEquals("user-1-files/", result2);
        assertEquals("folder/", result3);
    }

    @Test
    void shouldReturnValidNameOfCurrentFolderByPath() {
        String path = "user-1-files/folder/folder-2/";

        String result = FolderUtils.getNameOfCurrentFolderByPath(path);

        assertEquals("folder-2", result);
    }

    @Test
    void getTheNumberOfTheDuplicateFolderNameByPath() {
        String path = "user-1-files/123/11/11/12/11/";

        Long result = FolderUtils.getTheNumberOfTheDuplicateFolderNameByPath(path);

        assertEquals(3, result);
    }
}
