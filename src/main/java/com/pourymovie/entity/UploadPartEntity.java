package com.pourymovie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "upload_parts")
public class UploadPartEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private int partNumber;

  @Column private String eTag;

  @ManyToOne(fetch = FetchType.LAZY)
  private UploadSessionEntity session;
}
