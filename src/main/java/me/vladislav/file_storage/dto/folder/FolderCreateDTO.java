package me.vladislav.file_storage.dto.folder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FolderCreateDTO {

    private String rootFolderPath;

    @NotBlank(message = "Folder name cannot be empty.")
    @Size(min = 1, max = 50, message="Not valid size of folder name.")
    @Pattern(regexp = "^[a-zA-Z0-9._/ -]*$", message = "Name of folder must contain only alphanumeric characters and \'.\', \'-\', \'_\', \' \'.")
    private String nameOfNewFolder;

}
