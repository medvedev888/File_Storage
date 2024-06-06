package me.vladislav.file_storage.dto;

import lombok.Data;

@Data
public class MinioObjectDTO {
    private String path;
    private String name;
    private Boolean isFolder;
    private String owner;
}
