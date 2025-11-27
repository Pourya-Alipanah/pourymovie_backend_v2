package com.pourymovie.repository;

import com.pourymovie.entity.UploadCenterEntity;
import com.pourymovie.enums.UploadStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadCenterRepository extends JpaRepository<UploadCenterEntity, Long> {

  List<UploadCenterEntity> findAllByStatus(UploadStatus status);

  Optional<UploadCenterEntity> findByFileKey(String fileKey);
}
