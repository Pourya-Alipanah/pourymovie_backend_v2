package com.pourymovie.entity;

import com.pourymovie.enums.PersonRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "title_person"
)
public class TitlePeopleEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PersonRole role;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "titleId", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private TitleEntity title;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "personId", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private PeopleEntity person;
}
