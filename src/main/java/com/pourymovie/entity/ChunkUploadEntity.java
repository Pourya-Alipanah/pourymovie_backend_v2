package com.pourymovie.entity;

import com.pourymovie.enums.UploadStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chunk_uploads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChunkUploadEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String uploadId;

  @Column(nullable = false)
  private String fileKey;

  @Column(nullable = false)
  private String bucketName;

  @Column(nullable = false)
  private Long totalSize;

  @Column(nullable = false)
  private Integer totalChunks;

  @ElementCollection
  @CollectionTable(name = "chunk_parts", joinColumns = @JoinColumn(name = "upload_id"))
  @Column(name = "part_etag")
  @Builder.Default
  private List<String> uploadedParts = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Builder.Default
  private UploadStatus status = UploadStatus.PENDING;

  private String minioUploadId;

  @Column(nullable = false)
  @CreationTimestamp
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}
