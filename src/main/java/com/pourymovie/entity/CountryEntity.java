package com.pourymovie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "country")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
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
