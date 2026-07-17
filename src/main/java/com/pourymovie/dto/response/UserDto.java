package com.pourymovie.dto.response;

import com.pourymovie.enums.UserRole;

import java.time.LocalDateTime;

public record UserDto(
    long id,
    String firstName,
    String lastName,
    String email,
    String avatarUrl,
    UserRole role,
    LocalDateTime createdAt,
    LocalDateTime updateAt,
    boolean hasSubscription) {}
