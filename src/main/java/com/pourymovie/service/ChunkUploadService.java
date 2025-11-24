package com.pourymovie.service;

import com.github.slugify.Slugify;
import com.pourymovie.dto.request.InitiateChunkUploadDto;
import com.pourymovie.dto.response.ChunkUploadProgressResponse;
import com.pourymovie.entity.ChunkUploadEntity;
import com.pourymovie.enums.UploadStatus;
import com.pourymovie.provider.MinioProvider;
import com.pourymovie.repository.ChunkUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChunkUploadService {
  @Autowired
  private ChunkUploadRepository chunkUploadRepository;

  @Autowired
  private MinioProvider minioProvider;

  public Map<String, Object> initiateUpload(InitiateChunkUploadDto dto) throws Exception {
    String fileKey = buildObjectName(dto.fileName());
    String uploadId = UUID.randomUUID().toString();

    Map<String, String> minioData = minioProvider.initiateMultipartUpload(
            dto.bucketName(),
            fileKey
    );

    ChunkUploadEntity entity = ChunkUploadEntity.builder()
            .uploadId(uploadId)
            .fileKey(fileKey)
            .bucketName(dto.bucketName())
            .totalSize(dto.totalSize())
            .totalChunks(dto.totalChunks())
            .minioUploadId(minioData.get("uploadUrl"))
            .status(UploadStatus.PENDING)
            .build();

    chunkUploadRepository.save(entity);

    Map<String, Object> response = new HashMap<>();
    response.put("uploadId", uploadId);
    response.put("fileKey", fileKey);
    response.put("uploadedChunks", 0);
    response.put("initiateUrl", minioData.get("uploadUrl"));

    return response;
  }

  public Map<String, Object> getChunkUploadUrl(String uploadId, Integer chunkNumber) throws Exception {
    ChunkUploadEntity entity = chunkUploadRepository.findByUploadId(uploadId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Upload not found"));

    if (entity.getStatus() == UploadStatus.CONFIRMED) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Upload already completed");
    }

    String presignedUrl = minioProvider.generatePresignedChunkUploadUrl(
            entity.getBucketName(),
            entity.getFileKey(),
            chunkNumber,
            entity.getMinioUploadId(),
            60 // 60 minutes expiry
    );

    Map<String, Object> response = new HashMap<>();
    response.put("chunkNumber", chunkNumber);
    response.put("uploadUrl", presignedUrl);
    response.put("method", "PUT");

    return response;
  }

  public ChunkUploadProgressResponse markChunkUploaded(String uploadId, Integer chunkNumber, String etag) {
    ChunkUploadEntity entity = chunkUploadRepository.findByUploadId(uploadId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Upload not found"));

    while (entity.getUploadedParts().size() < chunkNumber) {
      entity.getUploadedParts().add(null);
    }

    entity.getUploadedParts().set(chunkNumber - 1, etag);
    chunkUploadRepository.save(entity);

    return new ChunkUploadProgressResponse(
            uploadId,
            (int) entity.getUploadedParts().stream().filter(Objects::nonNull).count(),
            entity.getTotalChunks(),
            entity.getStatus().name()
    );
  }

  public Map<String, Object> getCompleteUploadUrl(String uploadId) throws Exception {
    ChunkUploadEntity entity = chunkUploadRepository.findByUploadId(uploadId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Upload not found"));

    long uploadedCount = entity.getUploadedParts().stream().filter(Objects::nonNull).count();
    if (uploadedCount != entity.getTotalChunks()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
              "Not all chunks uploaded: " + uploadedCount + "/" + entity.getTotalChunks());
    }

    String completeUrl = minioProvider.generateCompleteMultipartUrl(
            entity.getBucketName(),
            entity.getFileKey(),
            entity.getMinioUploadId(),
            60
    );

    Map<String, Object> response = new HashMap<>();
    response.put("completeUrl", completeUrl);
    response.put("method", "POST");
    response.put("parts", buildPartsXml(entity.getUploadedParts()));

    return response;
  }

  public String finalizeUpload(String uploadId) {
    ChunkUploadEntity entity = chunkUploadRepository.findByUploadId(uploadId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Upload not found"));

    entity.setStatus(UploadStatus.CONFIRMED);
    chunkUploadRepository.save(entity);

    return minioProvider.getPublicUrl(entity.getBucketName(), entity.getFileKey());
  }

  public ChunkUploadProgressResponse getProgress(String uploadId) {
    ChunkUploadEntity entity = chunkUploadRepository.findByUploadId(uploadId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Upload not found"));

    long uploadedCount = entity.getUploadedParts().stream().filter(Objects::nonNull).count();

    return new ChunkUploadProgressResponse(
            uploadId,
            (int) uploadedCount,
            entity.getTotalChunks(),
            entity.getStatus().name()
    );
  }

  public Map<String, Object> getCancelUploadUrl(String uploadId) throws Exception {
    ChunkUploadEntity entity = chunkUploadRepository.findByUploadId(uploadId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Upload not found"));

    String abortUrl = minioProvider.generateAbortMultipartUrl(
            entity.getBucketName(),
            entity.getFileKey(),
            entity.getMinioUploadId(),
            60
    );

    Map<String, Object> response = new HashMap<>();
    response.put("abortUrl", abortUrl);
    response.put("method", "DELETE");

    return response;
  }

  public void deleteUploadRecord(String uploadId) {
    chunkUploadRepository.findByUploadId(uploadId)
            .ifPresent(chunkUploadRepository::delete);
  }

  public List<ChunkUploadEntity> getExpiredUploads() {
    LocalDateTime twelveHoursAgo = LocalDateTime.now().minusHours(12);
    return chunkUploadRepository.findAllByStatusAndCreatedAtBefore(
            UploadStatus.PENDING,
            twelveHoursAgo
    );
  }

  private String buildPartsXml(List<String> parts) {
    StringBuilder xml = new StringBuilder("<CompleteMultipartUpload>");
    for (int i = 0; i < parts.size(); i++) {
      if (parts.get(i) != null) {
        xml.append("<Part>")
                .append("<PartNumber>").append(i + 1).append("</PartNumber>")
                .append("<ETag>").append(parts.get(i)).append("</ETag>")
                .append("</Part>");
      }
    }
    xml.append("</CompleteMultipartUpload>");
    return xml.toString();
  }

  private String buildObjectName(String originalName) {
    String name = originalName.substring(0, originalName.lastIndexOf("."));
    String extension = originalName.substring(originalName.lastIndexOf("."));
    Slugify slg = new Slugify().withLowerCase(true);
    String safeName = slg.slugify(name);
    return System.currentTimeMillis() + "-" + safeName + extension;
  }
}
