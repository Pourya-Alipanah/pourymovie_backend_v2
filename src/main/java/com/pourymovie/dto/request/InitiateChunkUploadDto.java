package com.pourymovie.dto.request;

import com.pourymovie.enums.StreamBucketNames;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InitiateChunkUploadDto(
    @NotBlank String fileName,
    @NotNull int totalParts,
    @NotNull long totalSize,
    @NotNull StreamBucketNames bucket) {}
