package com.pourymovie.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CreateLanguageDto(@NotEmpty String nameFa, @NotEmpty String slug) {}
