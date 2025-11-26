package com.pourymovie.config;

import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3ClientConfig {
  @Autowired private AppDefaults appDefaults;

  @Bean
  public S3Client s3Client() {
    S3Configuration serviceConfig = S3Configuration.builder().pathStyleAccessEnabled(true).build();

    String protocol = appDefaults.isMinioSecure() ? "https://" : "http://";
    URI url = URI.create(protocol + appDefaults.getMinioUrl() + ":" + appDefaults.getMinioPort());

    return S3Client.builder()
        .endpointOverride(url)
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    appDefaults.getMinioAccessKey(), appDefaults.getMinioSecretKey())))
        .serviceConfiguration(serviceConfig)
        .region(Region.US_EAST_1)
        .build();
  }

  @Bean
  public S3Presigner s3Presigner() {
    S3Configuration serviceConfig = S3Configuration.builder().pathStyleAccessEnabled(true).build();

    String protocol = appDefaults.isMinioSecure() ? "https://" : "http://";
    URI url = URI.create(protocol + appDefaults.getMinioUrl() + ":" + appDefaults.getMinioPort());

    return S3Presigner.builder()
        .endpointOverride(url)
        .region(Region.US_EAST_1)
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(
                    appDefaults.getMinioAccessKey(), appDefaults.getMinioSecretKey())))
        .serviceConfiguration(serviceConfig)
        .build();
  }
}
