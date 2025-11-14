package com.pourymovie.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CreateGenreDto(
        @NotEmpty
        String nameFa,
        @NotEmpty
        String nameEn,
        @NotEmpty
        String slug
) {}
