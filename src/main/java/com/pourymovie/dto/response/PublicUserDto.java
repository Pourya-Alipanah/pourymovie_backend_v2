package com.pourymovie.dto.response;

public record PublicUserDto(
        Long id,
        String firstName,
        String lastName,
        String avatarUrl
) {}
