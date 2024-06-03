package me.vladislav.file_storage.configurations;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioClientConfiguration {

    @Value("${spring.minio.client.endpoint}")
    private String endpoint;

    @Value("${spring.minio.client.username}")
    private String username;

    @Value("${spring.minio.client.password}")
    private String password;

    @Bean
    public MinioClient minioClient() {

        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(username, password)
                .build();

        return minioClient;
    }

}
