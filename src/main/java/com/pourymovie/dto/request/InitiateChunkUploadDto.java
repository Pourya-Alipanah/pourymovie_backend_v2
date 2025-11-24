package com.pourymovie.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InitiateChunkUploadDto(
        @NotBlank
        String fileName,
        @NotBlank
        String bucketName,
        @NotNull
        @Min(1)
        Long totalSize,
        @NotNull
        @Min(1)
        Integer totalChunks,
        String contentType
) {
}
