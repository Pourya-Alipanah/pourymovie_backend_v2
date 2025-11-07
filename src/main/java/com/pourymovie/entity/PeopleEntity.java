package com.pourymovie.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "person"
)
public class PeopleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100)
  private String nameFa;

  @Column(length = 100)
  private String nameEn;

  @Column(length = 120 , unique = true)
  private String slug;

  @Column
  private LocalDate birthDate;

  @Column
  private LocalDate deathDate;

  @Column(length = 50)
  private String birthPlace;

  @Column(length = 500)
  private String imageUrl;

  @OneToMany(mappedBy = "person" , fetch = FetchType.LAZY)
  private List<TitlePeopleEntity> titlePersons;
}
