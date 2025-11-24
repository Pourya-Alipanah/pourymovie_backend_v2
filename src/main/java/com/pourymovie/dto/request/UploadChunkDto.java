package com.pourymovie.dto.request;

import com.pourymovie.enums.StreamBucketNames;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UploadChunkDto(
        @NotNull
        StreamBucketNames bucket,

        @NotBlank
        String sessionId,

        @NotNull
        int partNumber
) {
}
