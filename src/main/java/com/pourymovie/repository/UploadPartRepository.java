package com.pourymovie.repository;

import com.pourymovie.entity.UploadPartEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadPartRepository extends JpaRepository<UploadPartEntity, Long> {
  List<UploadPartEntity> findBySession_SessionIdOrderByPartNumberAsc(String sessionId);
}
