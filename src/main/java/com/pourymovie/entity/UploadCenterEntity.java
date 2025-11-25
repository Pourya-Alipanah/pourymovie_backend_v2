package com.pourymovie.entity;

import com.pourymovie.enums.AllBucketNames;
import com.pourymovie.enums.UploadFromEntity;
import com.pourymovie.enums.UploadStatus;
import com.pourymovie.enums.UploadType;
import com.pourymovie.persistence.BucketNameConverter;
import com.pourymovie.persistence.UploadFromEntityConverter;
import com.pourymovie.persistence.UploadStatusConverter;
import com.pourymovie.persistence.UploadTypeConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "uploads"
)
@Builder
public class UploadCenterEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String fileKey;

  @Column(nullable = false)
  @Convert(converter = BucketNameConverter.class)
  private AllBucketNames bucket;

  @Column
  @Convert(converter = UploadTypeConverter.class)
  private UploadType type;

  @Column
  @Convert(converter = UploadStatusConverter.class)
  private UploadStatus status = UploadStatus.PENDING;

  @Column
  @Convert(converter = UploadFromEntityConverter.class)
  private UploadFromEntity fromEntity;

  @CreationTimestamp
  private LocalDateTime createTime;
}
