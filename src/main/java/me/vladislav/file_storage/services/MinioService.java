package me.vladislav.file_storage.services;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.dto.MinioObjectDTO;
import me.vladislav.file_storage.dto.file.FileUploadDTO;
import me.vladislav.file_storage.dto.folder.FolderCreateDTO;
import me.vladislav.file_storage.dto.folder.FolderDeleteDTO;
import me.vladislav.file_storage.exceptions.file.FileUploadException;
import me.vladislav.file_storage.exceptions.folder.FolderCreationException;
import me.vladislav.file_storage.exceptions.folder.FolderDeletionException;
import me.vladislav.file_storage.exceptions.folder.RetrievingFoldersException;
import me.vladislav.file_storage.utils.FolderUtils;
import me.vladislav.file_storage.utils.MinioObjectUtils;
import me.vladislav.file_storage.utils.PathUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MinioService {

    private final MinioClient minioClient;

    @Value("${spring.minio.client.bucket-name}")
    private String bucketName;

    public List<MinioObjectDTO> getMinioObjects(String path, boolean recursive) {
        try {
            List<MinioObjectDTO> listOfFolders = new ArrayList<>();

            Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(path)
                    .recursive(recursive)
                    .build()
            );

            int k = 1;
            for (Result<Item> result : results) {
                Item item = result.get();

                String name = MinioObjectUtils.getNameOfCurrentObjectByPath(item.objectName());
                String currentPath = item.objectName();

                if (name.equals(MinioObjectUtils.getNameOfCurrentObjectByPath(currentPath)) && k == 1) {
                    k--;
                } else {
                    String owner = MinioObjectUtils.getOwnerFolder(currentPath,
                            name,
                            true,
                            FolderUtils.getTheNumberOfTheDuplicateFolderNameByPath(currentPath)
                    );
                    String currentPathWithoutCurrentFolder = PathUtils.getPathWithoutCurrentObject(
                            currentPath,
                            name,
                            true,
                            FolderUtils.getTheNumberOfTheDuplicateFolderNameByPath(currentPath)
                    );

                    listOfFolders.add(new MinioObjectDTO(
                            PathUtils.getPathWithoutRootUserFolder(currentPathWithoutCurrentFolder),
                            MinioObjectUtils.getObjectNameToDisplay(name),
                            owner,
                            item.objectName().endsWith("/")
                    ));
                }
            }

            return listOfFolders;
        } catch (Exception e) {
            throw new RetrievingFoldersException("Error when retrieving folders.", e);
        }
    }

    public void createFolder(FolderCreateDTO folderCreateDTO, boolean isCovertOperation) {
        String folderName = folderCreateDTO.getNameOfNewFolder();
        String rootFolderPath = folderCreateDTO.getRootFolderPath();

        if (folderName.charAt(folderName.length() - 1) != '/') {
            folderName += '/';
        }
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

    public void deleteFolder(FolderDeleteDTO folderDeleteDTO, Long userID) {
        String folderName = folderDeleteDTO.getFolderName();
        String rootFolderPath = folderDeleteDTO.getRootFolderPath();

        folderName = folderName.charAt(folderName.length() - 1) != '/' ? folderName += '/' : folderName;

        if (!isFolderWithThisNameExists(rootFolderPath, folderName)) {
            throw new FolderDeletionException("Error when deleting folder. Folder with name " + rootFolderPath + folderName + " not exists.");
        }

        List<MinioObjectDTO> folderList = getMinioObjects(rootFolderPath + folderName, true);
        List<DeleteObject> objectsForDeleting = new LinkedList<>();

        String name;
        for (MinioObjectDTO object : folderList) {
            name = object.getName();
            if (!name.endsWith("/")) {
                name += "/";
            }
            objectsForDeleting.add(new DeleteObject(
                    PathUtils.getRootPath(object.getRootPath() + name,
                            userID)
            ));
        }
        objectsForDeleting.add(new DeleteObject(rootFolderPath + folderName));

        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(bucketName)
                .objects(objectsForDeleting)
                .build()
        );

        try {
            for (Result<DeleteError> result : results) {
                result.get();
            }
        } catch (Exception e) {
            throw new FolderDeletionException("Error when deleting folder.", e);
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

    public void uploadFile(FileUploadDTO fileUploadDTO) {

        MultipartFile file = fileUploadDTO.getFile();
        String rootFolderPath = fileUploadDTO.getRootFolderPath();
        String fileName = rootFolderPath + file.getOriginalFilename();

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .stream(inputStream, file.getSize(), -1)
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            throw new FileUploadException("Error when uploading file.", e);
        }

    }

}
