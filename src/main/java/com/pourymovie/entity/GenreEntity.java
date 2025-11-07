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
@Table(name = "genre")
public class GenreEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 50)
  private String nameFa;

  @Column(length = 50)
  private String nameEn;

  @Column(length = 120, unique = true)
  private String slug;

  @ManyToMany(mappedBy = "genres")
  private List<TitleEntity> titles;
}
