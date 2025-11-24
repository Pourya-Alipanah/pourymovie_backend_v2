package com.pourymovie.dto.response;

public record ChunkUploadInitResponse(
        String uploadId,
        String fileKey,
        Integer uploadedChunks
) {
}
