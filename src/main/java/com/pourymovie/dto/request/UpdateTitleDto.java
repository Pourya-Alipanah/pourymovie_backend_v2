package com.pourymovie.dto.request;

import com.pourymovie.enums.TitleType;

import java.util.List;
import java.util.Set;

public record UpdateTitleDto(
        String titleFa,
        String titleEn,
        String slug,
        Long countryId,
        Set<Long> genreIds,
        Long languageId,
        Integer releaseYear,
        ConfirmUploadDto trailerUrl,
        ConfirmUploadDto coverUrl,
        ConfirmUploadDto thumbnailUrl,
        Set<CreateTitlePeopleDto> titlePeople,
        Integer durationMinutes,
        Float imdbRating,
        Integer imdbVotes,
        boolean isTop250,
        Integer top250Rank,
        String summary,
        String ageRating,
        boolean hasSubtitle,
        String awards,
        TitleType type,
        Set<Long> videoLinkIds
) {
}
