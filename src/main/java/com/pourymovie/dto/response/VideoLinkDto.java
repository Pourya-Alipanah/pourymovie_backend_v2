package com.pourymovie.dto.response;

import com.pourymovie.enums.VideoQuality;

public record VideoLinkDto(Long id, VideoQuality quality, String url) {}
