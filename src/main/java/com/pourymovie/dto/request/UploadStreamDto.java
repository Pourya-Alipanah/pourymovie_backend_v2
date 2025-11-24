package com.pourymovie.dto.request;

import com.pourymovie.enums.StreamBucketNames;
import jakarta.validation.constraints.NotNull;

public record UploadStreamDto(
        @NotNull
        StreamBucketNames bucket
) {
}
