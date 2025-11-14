package com.pourymovie.dto.response;

import java.util.List;

public record SeasonDto(
        Long id,
        Integer seasonNumber,
        List<EpisodeDto> episodes,
        boolean specialSeason,
        String specialSeasonName
) {}
