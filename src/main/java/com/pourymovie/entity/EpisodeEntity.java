package com.pourymovie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "episode")
public class EpisodeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Integer episodeNumber;

  @OneToMany(mappedBy = "episode", fetch = FetchType.LAZY)
  private List<VideoLinkEntity> videoLinks;

  @ManyToOne
  @JoinColumn(name = "seasonId")
  @JsonIgnore
  private SeasonEntity season;
}
