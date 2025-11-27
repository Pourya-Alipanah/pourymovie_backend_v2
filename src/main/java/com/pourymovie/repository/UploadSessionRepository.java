package com.pourymovie.repository;

import com.pourymovie.entity.UploadSessionEntity;
import com.pourymovie.enums.UploadStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadSessionRepository extends JpaRepository<UploadSessionEntity, Long> {
  Optional<UploadSessionEntity> findBySessionId(String sessionId);

  List<UploadSessionEntity> findByStatus(UploadStatus status);
}
