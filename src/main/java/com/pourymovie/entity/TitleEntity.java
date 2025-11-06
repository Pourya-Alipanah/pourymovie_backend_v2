package com.pourymovie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pourymovie.enums.TitleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

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

//  @ManyToOne(targetEntity = )
//  private Language language;

//  @ManyToMany(mappedBy = )
//  @JoinTable
//  private List<Genre> genres;

//  @Formula("(SELECT json_agg(p.*) FROM person p " +
//          "JOIN title_person tp ON tp.person_id = p.id " +
//          "WHERE tp.title_id = id AND tp.role = 'ACTOR')")
//  private String actorsJson;
//
//  @Transient
//  private List<Person> actors;
//
//  public List<Person> getActors() {
//    if (actors == null && actorsJson != null) {
//      try {
//        ObjectMapper mapper = new ObjectMapper();
//        actors = mapper.readValue(actorsJson, new TypeReference<List<Person>>() {});
//      } catch (Exception e) {
//        actors = List.of();
//      }
//    }
//    return actors;
//  }

//  @ManyToOne(targetEntity = .class)
//  private Country country;

  @OneToMany(targetEntity = SeasonEntity.class)
  private List<SeasonEntity> seasons;

//  @OneToMany(targetEntity = .class)
//  private VideoLinks videoLinks;

//  @OneToMany
//  @JsonIgnore
//  private List<TitlePerson> people;

//  @OneToMany(targetEntity = .class)
//  private List<Comment> comments;
}
