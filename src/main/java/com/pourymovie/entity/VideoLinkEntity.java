package com.pourymovie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pourymovie.enums.VideoQuality;
import com.pourymovie.persistence.VideoQualityConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(
    name = "video_link",
    indexes = {
      @Index(columnList = "quality,episodeId", name = "idx_quality_episode", unique = true),
      @Index(columnList = "quality,titleId", name = "idx_quality_title", unique = true)
    })
public class VideoLinkEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 500)
  private String url;

  @Column
  @Convert(converter = VideoQualityConverter.class)
  private VideoQuality quality;

  @ManyToOne
  @JoinColumn(name = "episodeId")
  @JsonIgnore
  private EpisodeEntity episode;

  @ManyToOne
  @JoinColumn(name = "titleId")
  @JsonIgnore
  private TitleEntity title;
}
