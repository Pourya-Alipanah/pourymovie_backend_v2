package com.pourymovie.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AiTitleSummaryDto(@NotBlank String name) {}
