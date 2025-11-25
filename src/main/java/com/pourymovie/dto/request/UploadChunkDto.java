package com.pourymovie.dto.request;

import com.pourymovie.enums.StreamBucketNames;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadChunkDto(
        @NotNull
        MultipartFile file,

        @NotNull
        StreamBucketNames bucket,

        @NotBlank
        String sessionId,

        @NotNull
        int partNumber
) {
}
