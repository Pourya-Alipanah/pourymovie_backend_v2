package com.pourymovie.dto.request;

import com.pourymovie.enums.BufferBucketNames;
import jakarta.validation.constraints.NotNull;

public record UploadBufferDto(
        @NotNull
        BufferBucketNames bucket
) {
}
