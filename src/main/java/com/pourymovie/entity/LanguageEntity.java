package com.pourymovie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "language")
public class LanguageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 10)
  private String nameFa;

  @Column(length = 20)
  private String slug;

  @OneToMany(mappedBy = "language", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<TitleEntity> titles;
}
