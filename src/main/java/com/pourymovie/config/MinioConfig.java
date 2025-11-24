package com.pourymovie.config;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
  @Autowired
  private AppDefaults appDefaults;

  @Bean
  public MinioClient minioClient() {
    return MinioClient.builder()
            .endpoint(
                    appDefaults.getMinioUrl(),
                    appDefaults.getMinioPort(),
                    appDefaults.isMinioSecure()
            )
            .credentials(
                    appDefaults.getMinioAccessKey(),
                    appDefaults.getMinioSecretKey()
            )
            .build();
  }
}
