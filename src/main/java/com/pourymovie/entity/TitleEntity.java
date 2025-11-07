package com.pourymovie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pourymovie.enums.TitleType;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;


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

  @OneToMany(mappedBy = "title", fetch = FetchType.EAGER)
  @SQLRestriction("role = 'actor'")
  private List<TitlePeopleEntity> actorLinks;

  @OneToMany(mappedBy = "title", fetch = FetchType.EAGER)
  @SQLRestriction("role = 'writer'")
  private List<TitlePeopleEntity> writerLinks;

  @OneToMany(mappedBy = "title", fetch = FetchType.EAGER)
  @SQLRestriction("role = 'director'")
  private List<TitlePeopleEntity> directorLinks;

  @Transient
  private List<PeopleEntity> actors;
  @Transient
  private List<PeopleEntity> directors;
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

  @Transient
  public List<PeopleEntity> getActors() {
    return actorLinks.stream().map(TitlePeopleEntity::getPerson).toList();
  }

  @Transient
  public List<PeopleEntity> getDirectors() {
    return directorLinks.stream().map(TitlePeopleEntity::getPerson).toList();
  }

  @Transient
  public List<PeopleEntity> getWriters() {
    return writerLinks.stream().map(TitlePeopleEntity::getPerson).toList();
  }
}
