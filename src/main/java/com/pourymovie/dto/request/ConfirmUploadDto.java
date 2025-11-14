package com.pourymovie.dto.request;

import com.pourymovie.enums.AllBucketNames;
import com.pourymovie.validation.ValidBucketName;
import jakarta.validation.constraints.NotEmpty;

public record ConfirmUploadDto(
        @ValidBucketName(enumClass = AllBucketNames.class)
        @NotEmpty
        AllBucketNames bucketName,

        @NotEmpty
        String key
) {
}
