package com.pourymovie.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PeopleDto{
  private Long id;
  private String nameFa;
  private String nameEn;
  private String slug;
  private String birthDate;
  private String deathDate;
  private String birthPlace;
  private String imageUrl;
}
