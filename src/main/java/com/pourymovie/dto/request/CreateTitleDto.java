package com.pourymovie.dto.request;

import com.pourymovie.enums.TitleType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

public record CreateTitleDto(
    @NotEmpty String titleFa,
    @NotEmpty String titleEn,
    @NotEmpty String slug,
    @NotNull Long countryId,
    Set<Long> genreIds,
    @NotNull Long languageId,
    @NotNull Integer releaseYear,
    @Valid ConfirmUploadDto trailerUrl,
    @Valid ConfirmUploadDto coverUrl,
    @Valid ConfirmUploadDto thumbnailUrl,
    @Valid Set<CreateTitlePeopleDto> titlePeople,
    @NotNull Integer durationMinutes,
    Float imdbRating,
    Integer imdbVotes,
    @NotNull boolean isTop250,
    Integer top250Rank,
    String summary,
    @NotEmpty String ageRating,
    @NotNull boolean hasSubtitle,
    String awards,
    @NotNull TitleType type,
    Set<Long> videoLinkIds) {}
