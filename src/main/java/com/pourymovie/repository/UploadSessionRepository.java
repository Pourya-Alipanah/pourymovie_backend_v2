package com.pourymovie.repository;

import com.pourymovie.entity.UploadSessionEntity;
import com.pourymovie.enums.UploadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UploadSessionRepository extends JpaRepository<UploadSessionEntity, Long> {
  Optional<UploadSessionEntity> findBySessionId(String sessionId);

  List<UploadSessionEntity> findByStatus(UploadStatus status);
}
