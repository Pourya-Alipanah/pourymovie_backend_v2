package com.pourymovie.dto.request;

import java.util.List;

public record CreateMultipleVideoLinkDto(List<CreateVideoLinkDto> data) {}
