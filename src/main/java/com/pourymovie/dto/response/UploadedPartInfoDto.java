package com.pourymovie.dto.response;

public record UploadedPartInfoDto(
        String eTag,
        int partNumber
) {
}
