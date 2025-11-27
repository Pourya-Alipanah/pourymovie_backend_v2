package com.pourymovie.dto.request;

import com.pourymovie.enums.BufferBucketNames;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadBufferDto(@NotNull MultipartFile file, @NotNull BufferBucketNames bucket) {}
