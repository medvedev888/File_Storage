package me.vladislav.file_storage.services;

import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.dto.MinioObjectDTO;
import me.vladislav.file_storage.dto.folder.FolderCreateDTO;
import me.vladislav.file_storage.dto.folder.FolderDeleteDTO;
import me.vladislav.file_storage.dto.folder.FoldersDTO;
import me.vladislav.file_storage.exceptions.folders.FolderCreationException;
import me.vladislav.file_storage.exceptions.folders.FolderDeletionException;
import me.vladislav.file_storage.exceptions.folders.RetrievingFoldersException;
import me.vladislav.file_storage.utils.FolderUtils;
import me.vladislav.file_storage.utils.PathUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final MinioClient minioClient;

    @Value("${spring.minio.client.bucket-name}")
    private String bucketName;

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

        List<MinioObjectDTO> folderList = getFolders(rootFolderPath + folderName, true).getListOfFolders();
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

    public FoldersDTO getFolders(String path, boolean recursive) {
        try {
            FoldersDTO foldersDTO = new FoldersDTO();
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

                String name = FolderUtils.getNameOfCurrentFolderByPath(item.objectName());
                String currentPath = item.objectName();

                if (name.equals(FolderUtils.getNameOfCurrentFolderByPath(currentPath)) && k == 1) {
                    k--;
                } else {
                    String owner = FolderUtils.getOwnerFolder(currentPath,
                            name,
                            true,
                            FolderUtils.getTheNumberOfTheDuplicateFolderNameByPath(currentPath)
                    );
                    String currentPathWithoutCurrentFolder = PathUtils.getPathWithoutCurrentFolder(
                            currentPath,
                            name,
                            true,
                            FolderUtils.getTheNumberOfTheDuplicateFolderNameByPath(currentPath)
                    );

                    listOfFolders.add(new MinioObjectDTO(
                            PathUtils.getPathWithoutRootUserFolder(currentPathWithoutCurrentFolder),
                            FolderUtils.getFolderNameToDisplay(name),
                            owner,
                            item.objectName().endsWith("/")
                    ));
                }
            }

            foldersDTO.setListOfFolders(listOfFolders);

            return foldersDTO;
        } catch (Exception e) {
            throw new RetrievingFoldersException("Error when retrieving folders.", e);
        }
    }
}
