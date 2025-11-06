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
@Table(name = "season")
public class SeasonEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Integer seasonNumber;

//  @OneToMany(mappedBy = )
//  private List<Episode> episodes;

//  @ManyToOne(targetEntity = )
//  private Title title;

  @Column
  private boolean specialSeason = false;

  @Column
  private String specialSeasonName;
}
