package me.vladislav.file_storage.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import me.vladislav.file_storage.dto.file.FileUploadDTO;
import me.vladislav.file_storage.exceptions.file.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minioClient;

    @Value("${spring.minio.client.bucket-name}")
    private String bucketName;

    public void uploadFile(FileUploadDTO fileUploadDTO) {

        MultipartFile file = fileUploadDTO.getFile();
        String rootFolderPath = fileUploadDTO.getRootFolderPath();

        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .stream(inputStream, file.getSize(), -1)
                    .bucket(bucketName)
                    .object(rootFolderPath + file.getName())
                    .build());
        } catch (Exception e) {
            throw new FileUploadException("Error when uploading file.", e);
        }

    }
}
