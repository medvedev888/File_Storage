package me.vladislav.file_storage.configurations;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MinioConfiguration {
    private String endpoint;
    private String username;
    private String password;

    @Bean
    public MinioClient minioClient(Environment env){
        endpoint = env.getProperty("spring.minio.client.endpoint");
        username = env.getProperty("spring.minio.client.username");
        password = env.getProperty("spring.minio.client.password");

        MinioClient minioClient = MinioClient
                .builder()
                .endpoint(endpoint)
                .credentials(username, password)
                .build();

        return minioClient;
    }

}
