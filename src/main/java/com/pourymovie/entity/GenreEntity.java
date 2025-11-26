package com.pourymovie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<TitleEntity> titles;
}
