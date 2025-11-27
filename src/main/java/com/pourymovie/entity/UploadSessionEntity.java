package com.pourymovie.entity;

import com.pourymovie.enums.StreamBucketNames;
import com.pourymovie.enums.UploadStatus;
import com.pourymovie.persistence.StreamBucketNameConverter;
import com.pourymovie.persistence.UploadStatusConverter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "upload_sessions"
)
public class UploadSessionEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String sessionId;

  @Column
  private String uploadId;

  @Column
  private String fileName;

  @Column
  @Convert(converter = UploadStatusConverter.class)
  private UploadStatus status;

  @Column
  private int totalParts;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @Column
  private LocalDateTime expireAt = LocalDateTime.now().plusHours(12);

  @Column
  private long totalSize;

  @Column(nullable = false)
  @Convert(converter = StreamBucketNameConverter.class)
  private StreamBucketNames bucket;

  @OneToMany(mappedBy = "session", cascade = CascadeType.ALL , orphanRemoval = true)
  private List<UploadPartEntity> uploadedParts = new ArrayList<>();

  @Transient
  public boolean isExpired() {
    return expireAt != null && expireAt.isBefore(LocalDateTime.now());
  }
}
