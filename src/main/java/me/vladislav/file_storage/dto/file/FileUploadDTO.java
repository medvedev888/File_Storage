package me.vladislav.file_storage.dto.file;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadDTO {

    //TODO: need to add validation
    private MultipartFile file;
    private String rootFolderPath;

}
