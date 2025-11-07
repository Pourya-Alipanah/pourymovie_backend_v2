package com.pourymovie.entity;

import com.pourymovie.enums.AllBucketNames;
import com.pourymovie.enums.UploadFromEntity;
import com.pourymovie.enums.UploadStatus;
import com.pourymovie.enums.UploadType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "uploads"
)
public class UploadCenterEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String fileKey;

  @Column
  @Enumerated(EnumType.STRING)
  private AllBucketNames bucketName;

  @Column
  @Enumerated(EnumType.STRING)
  private UploadType type;

  @Column
  @Enumerated(EnumType.STRING)
  private UploadStatus status = UploadStatus.PENDING;

  @Column
  @Enumerated(EnumType.STRING)
  private UploadFromEntity fromEntity;

  @CreationTimestamp
  private ZonedDateTime createTime;
}
