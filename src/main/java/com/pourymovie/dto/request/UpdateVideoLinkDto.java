package com.pourymovie.dto.request;

import com.pourymovie.enums.VideoQuality;

public record UpdateVideoLinkDto(
        ConfirmUploadDto url,

        VideoQuality quality,

        Long episodeId,

        Long titleId
) {
}
