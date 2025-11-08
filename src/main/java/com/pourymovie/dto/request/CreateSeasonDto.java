package com.pourymovie.dto.request;

import jakarta.validation.constraints.NotEmpty;


public record CreateSeasonDto(
        @NotEmpty
        Long titleId,
        @NotEmpty
        Integer seasonNumber,
        @NotEmpty
        boolean specialSeason,
        String specialSeasonName
) {}