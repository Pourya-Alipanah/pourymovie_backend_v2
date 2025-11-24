package com.pourymovie.repository;

import com.pourymovie.entity.ChunkUploadEntity;
import com.pourymovie.enums.UploadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChunkUploadRepository extends JpaRepository<ChunkUploadEntity, Long> {

  Optional<ChunkUploadEntity> findByUploadId(String uploadId);

  List<ChunkUploadEntity> findAllByStatusAndCreatedAtBefore(UploadStatus status, LocalDateTime date);
}
