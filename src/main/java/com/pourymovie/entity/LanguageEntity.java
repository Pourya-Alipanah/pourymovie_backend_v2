package com.pourymovie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

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

  @OneToMany(mappedBy = "language" , fetch =  FetchType.LAZY)
  @JsonIgnore
  private List<TitleEntity> titles;
}
