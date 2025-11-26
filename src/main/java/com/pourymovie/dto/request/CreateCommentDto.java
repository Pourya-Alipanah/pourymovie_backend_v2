package com.pourymovie.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateCommentDto(
    @NotEmpty @Size(min = 1, max = 250) String content,
    @NotEmpty @Size(min = 1, max = 100) String subject,
    @NotNull Long titleId) {}
