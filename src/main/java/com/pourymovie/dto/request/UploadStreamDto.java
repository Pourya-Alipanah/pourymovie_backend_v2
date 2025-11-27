package com.pourymovie.dto.request;

import com.pourymovie.enums.StreamBucketNames;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadStreamDto(@NotNull MultipartFile file, @NotNull StreamBucketNames bucket) {}
