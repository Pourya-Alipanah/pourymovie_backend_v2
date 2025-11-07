package com.pourymovie.dto.response;

import java.time.LocalDateTime;

public record CommentDto(
        Long id,
        String content,
        String subject,
        LocalDateTime createdAt,
        boolean isUpdated,
        PublicUserDto user
) {}
