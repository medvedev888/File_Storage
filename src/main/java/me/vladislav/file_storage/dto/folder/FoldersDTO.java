package me.vladislav.file_storage.dto.folder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.vladislav.file_storage.dto.MinioObjectDTO;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoldersDTO {

    private List<MinioObjectDTO> listOfFolders;
    private Map<String, Integer> mapOfDuplicates;

}