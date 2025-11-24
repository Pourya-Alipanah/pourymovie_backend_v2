package com.pourymovie.dto.response;

public record ChunkUploadProgressResponse(
        String uploadId,
        Integer uploadedChunks,
        Integer totalChunks,
        String status
) {
}
