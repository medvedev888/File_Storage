package me.vladislav.file_storage.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.exceptions.folders.FolderCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final MinioClient minioClient;

    @Value("${spring.minio.client.bucket-name}")
    private String bucketName;

    public void createFolder(String rootFolderPath, String folderName){
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(rootFolderPath + folderName + '/')
                    .stream(new ByteArrayInputStream(new byte[] {}), 0, -1)
                    .build());
        } catch (Exception e) {
            throw new FolderCreationException("Error when creating folder.", e);
        }
    }
}
