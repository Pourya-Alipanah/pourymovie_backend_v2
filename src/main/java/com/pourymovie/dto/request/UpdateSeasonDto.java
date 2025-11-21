package com.pourymovie.dto.request;


public record UpdateSeasonDto(
        Long titleId,
        Integer seasonNumber,
        boolean specialSeason,
        String specialSeasonName
) {}