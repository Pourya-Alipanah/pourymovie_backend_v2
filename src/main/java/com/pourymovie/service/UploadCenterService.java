package com.pourymovie.service;

import com.pourymovie.config.AppDefaults;
import com.pourymovie.dto.response.UploadResultDto;
import com.pourymovie.entity.UploadCenterEntity;
import com.pourymovie.enums.*;
import com.pourymovie.provider.MinioProvider;
import com.pourymovie.repository.UploadCenterRepository;
import com.pourymovie.util.MinioUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

@Service
public class UploadCenterService {
  @Autowired
  private MinioProvider minioProvider;

  @Autowired
  private UploadCenterRepository uploadCenterRepository;

  @Autowired
  private AppDefaults appDefaults;

  @PostConstruct
  public void init() {
    if (appDefaults.getMinioTempUploadDir() != null) {
      File dir = new File(appDefaults.getMinioTempUploadDir());
      if (!dir.exists()) dir.mkdirs();
    }
  }

  public UploadResultDto withBuffer(MultipartFile file, BufferBucketNames bucket) throws Exception {
    String objectName = MinioUtils.buildObjectName(Objects.requireNonNull(file.getOriginalFilename()));

    minioProvider.uploadBuffer(
            bucket.getValue(),
            objectName,
            file.getBytes(),
            file.getContentType()
    );

    String url = minioProvider.getPublicUrl(bucket.getValue(), objectName);

    UploadCenterEntity upload = UploadCenterEntity.builder()
            .fileKey(objectName)
            .bucket(bucket.getMain())
            .status(UploadStatus.PENDING)
            .build();

    uploadCenterRepository.save(upload);

    return new UploadResultDto(bucket.getValue(), objectName, url);
  }

  public UploadResultDto withStream(MultipartFile file, StreamBucketNames bucket) throws Exception {
    String objectName = MinioUtils.buildObjectName(Objects.requireNonNull(file.getOriginalFilename()));

    Path tempPath = Path.of(appDefaults.getMinioTempUploadDir(), objectName);
    Files.copy(file.getInputStream(), tempPath);

    long size = Files.size(tempPath);

    try (InputStream stream = Files.newInputStream(tempPath)) {
      minioProvider.uploadStream(
              bucket.getValue(),
              objectName,
              stream,
              size,
              file.getContentType()
      );
    }

    Files.deleteIfExists(tempPath);

    String url = bucket.equals(StreamBucketNames.VIDEO)
            ? minioProvider.generatePresignedDownloadUrl(bucket.getValue(), objectName)
            : minioProvider.getPublicUrl(bucket.getValue(), objectName);

    UploadCenterEntity upload = UploadCenterEntity.builder()
            .fileKey(objectName)
            .bucket(bucket.getMain())
            .status(UploadStatus.PENDING)
            .build();

    uploadCenterRepository.save(upload);

    return new UploadResultDto(bucket.getValue(), objectName, url);
  }

  public List<UploadCenterEntity> pendingUploads() {
    return uploadCenterRepository.findAllByStatus(UploadStatus.PENDING);
  }

  public String confirmUpload(String fileKey, UploadFromEntity entity, UploadType type) throws Exception {
    UploadCenterEntity record = uploadCenterRepository.findByFileKey(fileKey)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    record.setFromEntity(entity);
    record.setType(type);
    record.setStatus(UploadStatus.CONFIRMED);

    uploadCenterRepository.save(record);

    if (entity.equals(UploadFromEntity.VIDEO)) {
      return getBucketAndKeyCombinationFromBucketAndKey(record.getBucket().getValue(), record.getFileKey());
    }

    return minioProvider.getPublicUrl(record.getBucket().getValue(), record.getFileKey());
  }

  public void removeUpload(String fileKey) {
    uploadCenterRepository.findByFileKey(fileKey).ifPresent(record -> {
      uploadCenterRepository.delete(record);
      try {
        minioProvider.removeObject(record.getBucket().getValue(), record.getFileKey());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  /**
   * @param objectName in format "bucket/objectKey"
   *
   */
  public String getDownloadUrlFromBucketAndKeyCombination(String objectName) throws Exception {
    String[] split = objectName.split("/");
    return minioProvider.generatePresignedDownloadUrl(split[0], split[1]);
  }

  /**
   * @return "bucket/objectKey" combination
   *
   */
  public String getBucketAndKeyCombinationFromBucketAndKey(String bucket, String key) {
    return bucket + "/" + key;
  }

}
