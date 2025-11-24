package com.pourymovie.repository;

import com.pourymovie.entity.UploadPartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadPartRepository extends JpaRepository<UploadPartEntity, Long> {
  List<UploadPartEntity> findBySession_SessionIdOrderByPartNumberAsc(String sessionId);
}
