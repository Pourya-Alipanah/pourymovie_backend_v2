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
@Table(name = "country")
public class CountryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(length = 50)
  private String nameEn;

  @Column(length = 50)
  private String nameFa;

  @Column(unique = true)
  private String slug;

  @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<TitleEntity> titles;
}
