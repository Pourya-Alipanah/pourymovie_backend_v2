package com.pourymovie.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pourymovie.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(length = 96)
  private String firstName;

  @Column(length = 96)
  private String lastName;

  @Column(length = 96, unique = true, nullable = false)
  private String email;

  @Column(length = 500)
  private String avatarUrl;

  @JsonIgnore
  @Column(length = 96 , nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;

  @CreationTimestamp
  @Column(updatable = false)
  private ZonedDateTime createdAt;

  @UpdateTimestamp
  @Column
  private ZonedDateTime updateAt;

  @Column
  private ZonedDateTime deletedAt;

  @JsonIgnore
  @Column
  private String resetPasswordToken;

  @JsonIgnore
  @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private Instant resetPasswordTokenExpires;

  @Column(nullable = false)
  private boolean hasSubscription = false;

//  @OneToMany(mappedBy = "user")
//  private List<Comment> comments;
//
//  @OneToOne(fetch = FetchType.EAGER)
//  @JoinColumn(name = "active_subscription_id")
//  private Subscription activeSubscription;
//
//  @OneToMany
//  @JoinColumn(name = "subscription_history_id")
//  private List<Subscription> subscriptionHistory;
//
//  @OneToMany
//  @JoinColumn(name = "payment_history_id")
//  private List<Payment> paymentHistory;
}
