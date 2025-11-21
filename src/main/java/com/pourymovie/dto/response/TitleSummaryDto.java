package com.pourymovie.dto.response;

import com.pourymovie.enums.TitleType;

public record TitleSummaryDto(
        Long id,
        String titleFa,
        String titleEn,
        String slug,
        TitleType type,
        String thumbnailUrl
) {
}
