package com.pourymovie.dto.response;

import com.pourymovie.enums.TitleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TitleDto {
  private Long id;
  private String titleFa;
  private String titleEn;
  private String slug;
  private TitleType type;
  private Integer releaseYear;
  private Integer imdbRating;
  private boolean isTop250;
  private Integer top250Rank;
  private String summary;
  private boolean hasSubtitle;
  private String awards;
  private String trailerUrl;
  private String coverUrl;
  private String thumbnailUrl;
  private LanguageDto language;
  private List<GenreDto> genres;
  private CountryDto country;
  private List<PeopleDto> actors;
  private List<PeopleDto> directors;
  private List<PeopleDto> writers;
}
