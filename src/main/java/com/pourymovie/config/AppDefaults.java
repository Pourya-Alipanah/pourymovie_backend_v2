package com.pourymovie.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "pourymovie")
public class AppDefaults {
  private int defaultPageSize;
  private int defaultPageNumber;
  private int defaultAccessTokenTTlInMinutes;
  private int defaultRefreshTokenTTlInMinutes;
  private String jwtSecretKey;
  private String[] publicPaths;
  private String minioUrl;
  private String minioAccessKey;
  private String minioSecretKey;
  private int minioPort;
  private boolean minioSecure;
  private String minioExpirationInMinutes;
  private String minioTempUploadDir;
}