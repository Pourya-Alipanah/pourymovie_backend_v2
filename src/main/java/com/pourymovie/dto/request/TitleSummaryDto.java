package com.pourymovie.dto.request;

import jakarta.validation.constraints.NotBlank;

public record TitleSummaryDto(
        @NotBlank
        String name
) {
}
