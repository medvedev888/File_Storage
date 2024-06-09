package me.vladislav.file_storage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MinioObjectDTO {
    //TODO: need to add validation using regular expression
    private String path;
    private String name;
    private String owner;
    private Boolean isFolder;
}