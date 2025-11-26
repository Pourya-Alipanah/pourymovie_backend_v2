package com.pourymovie.dto.response;

import java.util.List;

public record EpisodeDto(Long id, Integer episodeNumber, List<VideoLinkDto> videoLinks) {}
