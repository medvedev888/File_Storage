package me.vladislav.file_storage.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FolderCreateDTO {

    private String rootFolderPath;
    //TODO: need to add validation using regular expression
    @NotBlank
    @Size(min = 1, max = 50, message="Not valid size of folder name")
    private String nameOfNewFolder;

}
