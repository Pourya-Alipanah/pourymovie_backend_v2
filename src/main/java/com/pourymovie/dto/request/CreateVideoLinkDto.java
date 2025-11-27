package com.pourymovie.dto.request;

import com.pourymovie.enums.VideoQuality;
import com.pourymovie.validation.ExactlyOneOf;
import jakarta.validation.constraints.NotNull;

@ExactlyOneOf(fields = {"episodeId", "titleId"})
public record CreateVideoLinkDto(
    @NotNull ConfirmUploadDto url, @NotNull VideoQuality quality, Long episodeId, Long titleId) {}
