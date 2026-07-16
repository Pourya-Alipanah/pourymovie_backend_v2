package com.pourymovie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pourymovie.enums.DeletedBy;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CommentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 100)
  private String subject;

  @Column(length = 250)
  private String content;

  @CreationTimestamp private LocalDateTime createdAt;

  @JsonIgnore @Column private LocalDateTime deletedAt;

  @JsonIgnore @UpdateTimestamp @Column private LocalDateTime updateAt;

  @JsonIgnore
  @Column
  @Enumerated(EnumType.STRING)
  private DeletedBy deletedBy;

  @Formula("\"createdAt\" <> \"updateAt\"")
  private boolean isUpdated;

  @ManyToOne
  @JoinColumn(name = "userId")
  @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "titleId")
  @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
  private TitleEntity title;
}
