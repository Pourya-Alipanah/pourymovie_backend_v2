package com.pourymovie.entity;

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

//  @OneToMany(mappedBy = "")
//  private List<VideoLink> videoLinks;

//  @ManyToOne(targetEntity = SeasonEntity.class)
//  private SeasonEntity season;
}
