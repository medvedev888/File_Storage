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

}
