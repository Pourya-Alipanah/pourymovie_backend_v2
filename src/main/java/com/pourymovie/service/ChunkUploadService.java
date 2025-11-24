package com.pourymovie.service;

import com.pourymovie.dto.request.InitiateChunkUploadDto;
import com.pourymovie.dto.request.UploadChunkDto;
import com.pourymovie.dto.response.ChunkUploadDto;
import com.pourymovie.dto.response.UploadResultDto;
import com.pourymovie.dto.response.UploadedPartInfoDto;
import com.pourymovie.entity.UploadCenterEntity;
import com.pourymovie.entity.UploadPartEntity;
import com.pourymovie.entity.UploadSessionEntity;
import com.pourymovie.enums.UploadStatus;
import com.pourymovie.mapper.UploadSessionMapper;
import com.pourymovie.provider.ChunkUploadProvider;
import com.pourymovie.provider.MinioProvider;
import com.pourymovie.repository.UploadCenterRepository;
import com.pourymovie.repository.UploadPartRepository;
import com.pourymovie.repository.UploadSessionRepository;
import com.pourymovie.util.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ChunkUploadService {
  @Autowired
  private ChunkUploadProvider chunkUploadProvider;

  @Autowired
  private MinioProvider minioProvider;

  @Autowired
  private UploadPartRepository uploadPartRepository;

  @Autowired
  private UploadSessionRepository uploadSessionRepository;

  @Autowired
  private UploadCenterRepository uploadCenterRepository;

  @Autowired
  private UploadSessionMapper uploadSessionMapper;


  public ChunkUploadDto initiateChunkUpload(InitiateChunkUploadDto initiateChunkUploadDto) {
    UploadSessionEntity session = uploadSessionMapper.toEntity(initiateChunkUploadDto);

    String uploadId = chunkUploadProvider.initiateUpload(
            session.getBucket().getValue(),
            session.getFileName()
    );

    session.setUploadId(uploadId);
    session.setSessionId(UUID.randomUUID().toString());
    uploadSessionRepository.save(session);
    return uploadSessionMapper.toDto(session);
  }

  @Transactional
  public void uploadPart(UploadChunkDto uploadChunkDto, MultipartFile file) throws IOException {
    UploadSessionEntity session = uploadSessionRepository.findBySessionId(uploadChunkDto.sessionId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    String objectName = MinioUtils.buildObjectName(Objects.requireNonNull(file.getOriginalFilename()));

    String eTag = chunkUploadProvider.uploadPart(
            session.getUploadId(),
            objectName,
            uploadChunkDto.partNumber(),
            session.getBucket().getValue(),
            file
    );

    UploadPartEntity.builder()
            .session(session)
            .partNumber(uploadChunkDto.partNumber())
            .eTag(eTag)
            .build();
  }

  public List<UploadedPartInfoDto> listUploadedParts(String sessionId) {
    List<UploadPartEntity> parts = uploadPartRepository.findBySession_SessionIdOrderByPartNumberAsc(sessionId);

    return parts.stream()
            .map(part ->
                    new UploadedPartInfoDto(part.getETag(), part.getPartNumber())
            )
            .toList();
  }

  @Transactional
  public UploadResultDto completeUpload(String sessionId) throws Exception {
    UploadSessionEntity session = uploadSessionRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    List<UploadPartEntity> parts = uploadPartRepository.findBySession_SessionIdOrderByPartNumberAsc(sessionId);

    chunkUploadProvider.completeUpload(
            session.getUploadId(),
            parts,
            session.getFileName(),
            session.getBucket().getValue()
    );

    String url = minioProvider.generatePresignedDownloadUrl(
            session.getBucket().getValue(),
            session.getFileName()
    );

    var result = new UploadResultDto(session.getBucket().getValue(), session.getFileName(), url);

    UploadCenterEntity upload = UploadCenterEntity.builder()
            .fileKey(session.getFileName())
            .bucket(session.getBucket().getMain())
            .status(UploadStatus.PENDING)
            .build();

    uploadCenterRepository.save(upload);

    uploadSessionRepository.delete(session);

    return result;
  }

  public void abortUpload(String sessionId) throws Exception {
    UploadSessionEntity session = uploadSessionRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    chunkUploadProvider.abortUpload(
            session.getUploadId(),
            session.getBucket().getValue(),
            session.getFileName()
    );

    uploadSessionRepository.delete(session);
  }

  public List<UploadSessionEntity> getExpiredUploads() {
    return uploadSessionRepository.findByStatus(UploadStatus.PENDING).stream().filter(
            UploadSessionEntity::isExpired
    ).toList();
  }
}
