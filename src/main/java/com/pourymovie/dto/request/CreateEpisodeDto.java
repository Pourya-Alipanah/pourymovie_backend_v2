package com.pourymovie.dto.request;

import jakarta.validation.constraints.NotNull;

public record CreateEpisodeDto(
        @NotNull
        Integer episodeNumber,

        @NotNull
        Long seasonId
) {
}
