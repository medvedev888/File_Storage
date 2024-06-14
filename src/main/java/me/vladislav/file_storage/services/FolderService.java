package me.vladislav.file_storage.services;

import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.dto.MinioObjectDTO;
import me.vladislav.file_storage.exceptions.folders.FolderCreationException;
import me.vladislav.file_storage.exceptions.folders.RetrievingFoldersException;
import me.vladislav.file_storage.utils.FolderUtils;
import me.vladislav.file_storage.utils.PathUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final MinioClient minioClient;

    @Value("${spring.minio.client.bucket-name}")
    private String bucketName;

    public void createFolder(String rootFolderPath, String folderName, boolean isCovertOperation) {
        folderName += '/';
        if (isFolderWithThisNameExists(rootFolderPath, folderName) && !isCovertOperation) {
            throw new FolderCreationException("Error when creating folder. Folder with this name exists.");
        }
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(rootFolderPath + folderName)
                    .stream(new ByteArrayInputStream(new byte[]{}), 0, -1)
                    .build());
        } catch (Exception e) {
            throw new FolderCreationException("Error when creating folder.", e);
        }
    }

    public boolean isFolderWithThisNameExists(String path, String folderName) {
        try {
            minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(path + folderName)
                            .build());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<MinioObjectDTO> getFolders(String path, boolean recursive) {
        try {
            List<MinioObjectDTO> listOfFolders = new ArrayList<>();

            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(path)
                    .recursive(recursive)
                    .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                String name = FolderUtils.getNameOfCurrentFolderByPath(item.objectName());

                if (!name.equals(FolderUtils.getNameOfCurrentFolderByPath(path))) {

                    String owner = FolderUtils.getOwnerFolder(path, name);

                    listOfFolders.add(new MinioObjectDTO(PathUtils.getPathWithoutRootUserFolder(path), name, owner, item.objectName().endsWith("/")));
                }
            }
            return listOfFolders;
        } catch (Exception e) {
            throw new RetrievingFoldersException("Error when retrieving folders.", e);
        }
    }
}
