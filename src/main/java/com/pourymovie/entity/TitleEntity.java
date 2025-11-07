package com.pourymovie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pourymovie.enums.TitleType;
import com.pourymovie.util.DBUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "title",
        indexes = {
          @Index(name = "idx_title_en", columnList = "titleEn"),
        }
)
public class TitleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  @Enumerated(EnumType.STRING)
  private TitleType titleType;

  @Column(length = 100)
  private String titleFa;

  @Column(length = 100)
  private String titleEn;

  @Column(length = 120 , unique = true)
  private String slug;

  @Column
  private Integer releaseYear;

  @Column
  private Integer durationMinutes;

  @Column
  private Float imdbRating;

  @Column
  private Integer imdbVotes;

  @Column
  private boolean isTop250;

  @Column
  private Integer top250Rank;

  @Column(length = 500)
  private String summary;

  @Column(length = 10)
  private String ageRating;

  @Column
  private boolean hasSubtitle = false;

  @Column
  private String awards;

  @Column
  private String trailerUrl;

  @Column
  private String coverUrl;

  @Column
  private String thumbnailUrl;

  @ManyToOne
  @JoinColumn(name = "languageId")
  private LanguageEntity language;

  @ManyToMany
  @JoinTable(
          name = "title_genres_genre",
          joinColumns = @JoinColumn(name = "titleId"),
          inverseJoinColumns = @JoinColumn(name = "genreId")
  )
  private List<GenreEntity> genres;

  @JsonIgnore
  @Formula("(SELECT json_agg(p.*) FROM person p " +
          "JOIN title_person tp ON tp.person_id = p.id " +
          "WHERE tp.title_id = id AND tp.role = 'ACTOR')")
  private String actorsJson;

  @Transient
  private List<PeopleEntity> actors;

  @JsonIgnore
  @Formula("(SELECT json_agg(p.*) FROM person p " +
          "JOIN title_person tp ON tp.person_id = p.id " +
          "WHERE tp.title_id = id AND tp.role = 'DIRECTOR')")
  private String directorsJson;

  @Transient
  private List<PeopleEntity> directors;

  @JsonIgnore
  @Formula("(SELECT json_agg(p.*) FROM person p " +
          "JOIN title_person tp ON tp.person_id = p.id " +
          "WHERE tp.title_id = id AND tp.role = 'WRITER')")
  private String writersJson;

  @Transient
  private List<PeopleEntity> writers;

  @ManyToOne
  @JoinColumn(name = "countryId")
  @OnDelete(action = OnDeleteAction.SET_NULL)
  private CountryEntity country;

  @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<SeasonEntity> seasons;

  @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<VideoLinkEntity> videoLinks;

  @JsonIgnore
  @OneToMany(mappedBy = "title")
  private List<TitlePeopleEntity> people;

  @OneToMany(mappedBy = "title", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CommentEntity> comments;

  public List<PeopleEntity> getActors() {
    actors = DBUtils.safeJsonToList(actorsJson, new TypeReference<>() {});
    return actors;
  }

  public List<PeopleEntity> getDirectors() {
    directors = DBUtils.safeJsonToList(directorsJson, new TypeReference<>() {});
    return directors;
  }

  public List<PeopleEntity> getWriters() {
    writers = DBUtils.safeJsonToList(writersJson, new TypeReference<>() {});
    return writers;
  }
}
