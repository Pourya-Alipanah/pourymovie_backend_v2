package com.pourymovie.dto.request;


public record UpdateEpisodeDto(
        Integer episodeNumber,
        Long seasonId
) {
}
