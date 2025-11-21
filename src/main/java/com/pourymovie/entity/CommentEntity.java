package com.pourymovie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pourymovie.enums.DeletedBy;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "comment"
)
public class CommentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100)
  private String subject;

  @Column(length = 250)
  private String content;

  @CreationTimestamp
  private LocalDateTime createdAt;

  @JsonIgnore
  @Column
  private LocalDateTime deletedAt;

  @JsonIgnore
  @UpdateTimestamp
  @Column
  private LocalDateTime updateAt;

  @JsonIgnore
  @Column
  @Enumerated(EnumType.STRING)
  private DeletedBy deletedBy;

  @Formula("\"createdAt\" <> \"updateAt\"")
  private boolean isUpdated;

  @ManyToOne
  @JoinColumn(name = "userId")
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "titleId")
  private TitleEntity title;
}
